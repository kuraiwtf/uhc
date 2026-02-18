package dev.kurai.uhc.game.start.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameStartEvent;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.game.start.countdown.task.StartCountdownTask;
import dev.kurai.uhc.game.start.phase.StartPhase;
import dev.kurai.uhc.listener.game.PlayingListener;
import dev.kurai.uhc.module.power.listener.PowerListener;
import dev.kurai.uhc.module.power.task.updater.CooldownUpdaterTask;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.scoreboard.sidebar.adapter.builtin.PlayingSidebarAdapter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class StartServiceImpl implements StartService {

  private final UltraHardcoreAPI ultraHardcore;

  private final LinkedList<@NotNull StartPhase> phases;
  private StartPhase phase;

  private BukkitTask startTask;

  public StartServiceImpl(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    this.phases = Lists.newLinkedList();
  }

  @Override
  public Collection<@NotNull StartPhase> getPhases() {
    return List.copyOf(this.phases);
  }

  @Override
  public void clearPhases() {
    this.phases.clear();
  }

  @Override
  public void registerPhase(final @NotNull StartPhase phase) {
    this.phases.add(phase);
  }

  @Override
  public void registerPhase(final int index, final @NotNull StartPhase phase) {
    this.phases.add(index, phase);
  }

  @Override
  public void registerPhaseBefore(final @NotNull StartPhase from, final @NotNull StartPhase phase) {
    Preconditions.checkArgument(
        this.phases.contains(from), "Phase %s is not registered.", from.getId());
    this.phases.add(this.phases.indexOf(from) - 1, phase);
  }

  @Override
  public void registerPhaseAfter(final @NotNull StartPhase from, final @NotNull StartPhase phase) {
    Preconditions.checkArgument(
        this.phases.contains(from), "Phase %s is not registered.", from.getId());
    this.phases.add(this.phases.indexOf(from) + 1, phase);
  }

  @Override
  public void unregisterPhase(final @NotNull String id) {
    this.phases.removeIf(phase -> phase.getId().equals(id));
  }

  @Override
  public boolean hasPhase(final @NotNull String id) {
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
                this.ultraHardcore.getBukkitAudiences(),
                this.ultraHardcore.getGameService().getScatterService())
            .runTaskTimer(this.ultraHardcore.getPlugin(), 0, 20L);
  }

  @Override
  public void handleFinalStart() {
    this.startTask = null;

    final var eventService = this.ultraHardcore.getEventService();
    eventService.dispatchEvent(new GameStartEvent());
    eventService.registerListeners(
        new PlayingListener(this.ultraHardcore),
        new PowerListener(
            this.ultraHardcore.getProfileService(),
            this.ultraHardcore.getModuleService(),
            this.ultraHardcore.getPlugin()));

    final var gameService = this.ultraHardcore.getGameService();
    gameService.setStartTime(System.currentTimeMillis());
    gameService.getTimerService().startAllTimers();

    final var world = this.ultraHardcore.getWorldService().getWorld();
    final var worldBorder = world.getWorldBorder();
    final var initialSize = BorderConfiguration.INITIAL_SIZE_OPTION.getValue() * 2;
    worldBorder.setSize(initialSize);
    worldBorder.setCenter(0, 0);

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            this.ultraHardcore.getPlugin(),
            new CooldownUpdaterTask(this.ultraHardcore.getProfileService()),
            0,
            1L);

    final var profileService = this.ultraHardcore.getProfileService();
    for (final var profile : profileService.getProfiles()) {
      final var player = profile.getPlayer();
      if (player == null) {
        continue;
      }

      profile.setState(new PlayingProfileState());

      final var inventory = player.getInventory();
      inventory.setContents(InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue());
      inventory.setArmorContents(InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue());
    }

    this.ultraHardcore
        .getSidebarService()
        .installAdapter(new PlayingSidebarAdapter(this.ultraHardcore));
  }
}
