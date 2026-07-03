package dev.kurai.uhc.game.death;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.death.GameDeathEvent;
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
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    this.deathProcessor =
        context -> {
          final GameService gameService = ultraHardcore.gameService();
          gameService.playSound(
              Sound.sound()
                  .source(Sound.Source.HOSTILE)
                  .type(key("mob.wither.death"))
                  .volume(1f)
                  .pitch(1f)
                  .build());

          final PlayerDeathEvent event = context.event();

          final Player player = event.getEntity();
          final Player killer = player.getKiller();
          if (killer != null) {
            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            killer.giveExpLevels(3);
          }

          final Profile profile =
              ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
          this.eliminate(
              profile,
              killer == null
                  ? null
                  : this.ultraHardcore.profileService().getOrCreateProfile(killer),
              false);
        };
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

    new GameDeathEvent(killer, profile).callEvent();

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

    profile.removeComponent(DisconnectComponent.class);
    profile.removeComponent(PlayerInformationComponent.class);
    profile.removeComponent(ProcessingDeathComponent.class);

    profile.addComponent(
        new DeadComponent(
            killer == null ? null : killer.getId(),
            System.currentTimeMillis() - gameService.startTime(),
            component.lastLocation().clone(),
            component.inventory().clone(),
            component.armor().clone(),
            droppedItems));

    profile.setState(new DeadProfileState());
  }

  private void dropAt(
      final Location location, final @Nullable ItemStack item, final List<UUID> droppedItems) {
    if (item == null || item.getType() == Material.AIR) {
      return;
    }

    droppedItems.add(location.getWorld().dropItem(location, item).getUniqueId());
  }
}
