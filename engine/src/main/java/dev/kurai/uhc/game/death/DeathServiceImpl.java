package dev.kurai.uhc.game.death;

import static dev.kurai.uhc.util.CC.center;
import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.death.GameDeathEvent;
import dev.kurai.uhc.event.defaults.game.death.GamePreDeathEvent;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DeadComponent;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.component.PlayerInformationComponent;
import dev.kurai.uhc.profile.component.ProcessingDeathComponent;
import dev.kurai.uhc.profile.state.DeadProfileState;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@Getter
@Setter
public final class DeathServiceImpl implements DeathService {

  private final UltraHardcoreAPI ultraHardcore;

  private DeathAnnounce deathAnnounce;
  private DeathProcessor deathProcessor;

  public DeathServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;

    this.deathAnnounce = new BuiltinDeathAnnounce();
    this.deathProcessor = new BuiltinDeathProcessor(ultraHardcore, this);
  }

  @Override
  public void processDeath(final DeathContext context) {
    final GamePreDeathEvent event =
        new GamePreDeathEvent(context.profile(), context.killer(), context);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      this.revive(context.profile(), event.getRespawnLocation());
      return;
    }

    this.deathProcessor.processDeath(context);
  }

  private void revive(final Profile profile, final @Nullable Location location) {
    profile.executeAction(
        player -> {
          final Location target = location != null ? location : this.randomLocation();
          player.spigot().respawn();
          player.teleport(target);
        });

    profile.removeComponent(ProcessingDeathComponent.class);
    profile.removeComponent(PlayerInformationComponent.class);
  }

  private Location randomLocation() {
    final World world = this.ultraHardcore.worldService().getWorld();
    final int radius = (int) (world.getWorldBorder().getSize() / 2);
    return this.ultraHardcore
        .gameService()
        .scatterService()
        .getPositionProvider()
        .provideLocations(radius, 1)
        .iterator()
        .next();
  }

  @Override
  public void eliminate(
      final Profile profile, final @Nullable Profile killer, final boolean offline) {
    profile.executeAction(
        player -> {
          player.spigot().respawn();
          player.setGameMode(GameMode.SPECTATOR);
          player.teleport(this.ultraHardcore.worldService().getWorld().getSpawnLocation());
        });

    Bukkit.getPluginManager().callEvent(new GameDeathEvent(killer, profile));

    final PlayerInformationComponent component =
        profile.getComponent(PlayerInformationComponent.class);
    if (component == null) {
      return;
    }

    final Location location = component.lastLocation();
    final List<UUID> droppedItems = Lists.newArrayList();
    for (final ItemStack stack : component.inventory()) {
      this.dropAt(location, stack, droppedItems);
    }

    for (final ItemStack stack : component.armor()) {
      this.dropAt(location, stack, droppedItems);
    }

    if (killer != null) {
      killer.kills(killer.kills() + 1);
    }

    final GameService gameService = this.ultraHardcore.gameService();
    gameService.playSound(
        Sound.sound()
            .source(Sound.Source.HOSTILE)
            .type(key("mob.wither.death"))
            .volume(1f)
            .pitch(1f)
            .build());

    gameService.sendMessage(this.deathAnnounce.provideDeathMessage(profile, killer, offline));

    this.processVictimDeath(profile, killer, component, droppedItems);
    this.processWin();
  }

  private void processVictimDeath(
      final Profile profile,
      final @Nullable Profile killer,
      final PlayerInformationComponent informationComponent,
      final List<UUID> droppedItems) {
    profile.removeComponent(DisconnectComponent.class);
    profile.removeComponent(PlayerInformationComponent.class);
    profile.removeComponent(ProcessingDeathComponent.class);

    profile.addComponent(
        new DeadComponent(
            killer == null ? null : killer.getId(),
            System.currentTimeMillis() - this.ultraHardcore.gameService().startTime(),
            informationComponent.lastLocation().clone(),
            informationComponent.inventory().clone(),
            informationComponent.armor().clone(),
            droppedItems));

    profile.setState(new DeadProfileState());

    profile.sendMessage("");
    profile.sendMessage(center("&d&l»&r &lSPECTATEUR&d &l«"));
    profile.sendMessage(center("Suite à votre&c mort&r, vous êtes devenu"));
    profile.sendMessage(center("un&d spectateur&r de la&d partie&r."));
    profile.sendMessage("");
    profile.sendMessage(center("Utilisez la&c boussole&r dans votre&a inventaire"));
    profile.sendMessage(center("pour accéder à l'interface&d spectateur&r."));
    profile.sendMessage("");
  }

  private void processWin() {}

  private void dropAt(
      final Location location, final @Nullable ItemStack item, final List<UUID> droppedItems) {
    if (item == null || item.getType() == Material.AIR) {
      return;
    }

    droppedItems.add(location.getWorld().dropItem(location, item).getUniqueId());
  }
}
