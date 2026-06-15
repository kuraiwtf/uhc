package dev.kurai.uhc.game.start;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameStartEvent;
import dev.kurai.uhc.event.defaults.game.GameTickEvent;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.game.start.countdown.task.StartCountdownTask;
import dev.kurai.uhc.game.start.phase.StartPhase;
import dev.kurai.uhc.game.start.service.StartService;
import dev.kurai.uhc.listener.game.PlayingListener;
import dev.kurai.uhc.listener.game.SpectatorListener;
import dev.kurai.uhc.module.power.listener.PowerListener;
import dev.kurai.uhc.module.power.task.updater.CooldownUpdaterTask;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.profile.state.WaitingProfileState;
import dev.kurai.uhc.scoreboard.sidebar.adapter.builtin.PlayingSidebarAdapter;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

public final class StartServiceImpl implements StartService {

  private final UltraHardcoreAPI ultraHardcore;

  private final LinkedList<StartPhase> phases;
  private StartPhase phase;

  private BukkitTask startTask;

  public StartServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    this.phases = Lists.newLinkedList();
  }

  @Override
  public Collection<StartPhase> getPhases() {
    return List.copyOf(this.phases);
  }

  @Override
  public void clearPhases() {
    this.phases.clear();
  }

  @Override
  public void registerPhase(final StartPhase phase) {
    this.phases.add(phase);
  }

  @Override
  public void registerPhase(final int index, final StartPhase phase) {
    this.phases.add(index, phase);
  }

  @Override
  public void registerPhaseBefore(final StartPhase from, final StartPhase phase) {
    Preconditions.checkArgument(
        this.phases.contains(from), "Phase %s is not registered.", from.getId());
    this.phases.add(this.phases.indexOf(from) - 1, phase);
  }

  @Override
  public void registerPhaseAfter(final StartPhase from, final StartPhase phase) {
    Preconditions.checkArgument(
        this.phases.contains(from), "Phase %s is not registered.", from.getId());
    this.phases.add(this.phases.indexOf(from) + 1, phase);
  }

  @Override
  public void unregisterPhase(final String id) {
    this.phases.removeIf(phase -> phase.getId().equals(id));
  }

  @Override
  public boolean hasPhase(final String id) {
    return this.phases.stream().anyMatch(phase -> phase.getId().equals(id));
  }

  @Override
  public boolean isStarting() {
    return this.startTask != null;
  }

  @Override
  public void cancelStart() {
    if (this.startTask != null) {
      this.startTask.cancel();
      this.startTask = null;
    }
  }

  @Override
  public void handleStart() {
    this.startTask =
        new StartCountdownTask(
                this.ultraHardcore.bukkitAudiences(),
                this.ultraHardcore.gameService().scatterService())
            .runTaskTimer(this.ultraHardcore.plugin(), 0, 20L);
  }

  @Override
  public void handleFinalStart() {
    this.startTask = null;

    final var eventService = this.ultraHardcore.eventService();
    eventService.dispatchEvent(new GameStartEvent());
    eventService.registerListeners(
        new PlayingListener(this.ultraHardcore),
        new SpectatorListener(this.ultraHardcore),
        new PowerListener(
            this.ultraHardcore.profileService(),
            this.ultraHardcore.moduleService(),
            this.ultraHardcore.plugin()));

    final var gameService = this.ultraHardcore.gameService();
    gameService.startTime(System.currentTimeMillis());
    gameService.timerService().startAllTimers();
    gameService.cycleService().start();
    gameService.disconnectService().start();
    gameService.episodeService().start();

    final var world = this.ultraHardcore.worldService().getWorld();
    final var worldBorder = world.getWorldBorder();
    final var initialSize = BorderConfiguration.INITIAL_SIZE_OPTION.getValue() * 2;
    worldBorder.setSize(initialSize);
    worldBorder.setCenter(0, 0);

    Bukkit.getScheduler()
        .runTaskTimer(
            this.ultraHardcore.plugin(),
            () -> Bukkit.getPluginManager().callEvent(new GameTickEvent()),
            0,
            1L);
    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            this.ultraHardcore.plugin(),
            new CooldownUpdaterTask(this.ultraHardcore.profileService()),
            0,
            1L);

    final var profileService = this.ultraHardcore.profileService();
    for (final var profile : profileService.getProfiles()) {
      final var player = profile.getPlayer();
      if (player == null) {
        continue;
      }

      if (!(profile.getState() instanceof WaitingProfileState)
          || profile.hasComponent(SpectatorComponent.class)) {
        player.teleport(world.getSpawnLocation());
        player.setGameMode(GameMode.SPECTATOR);
        final PlayerInventory inventory = player.getInventory();
        inventory.setContents(new ItemStack[36]);
        inventory.setArmorContents(new ItemStack[4]);
        continue;
      }

      profile.setState(new PlayingProfileState());
      profile.addComponent(
          new DisconnectComponent(gameService.disconnectService().disconnectTime(), Instant.now()));

      final var inventory = player.getInventory();
      inventory.setContents(InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue());
      inventory.setArmorContents(InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue());
    }

    this.ultraHardcore
        .sidebarService()
        .installAdapter(new PlayingSidebarAdapter(this.ultraHardcore));
  }
}
