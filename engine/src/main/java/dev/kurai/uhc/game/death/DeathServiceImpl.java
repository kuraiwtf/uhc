package dev.kurai.uhc.game.death;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.profile.component.DeadComponent;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DeathServiceImpl implements DeathService {

  private DeathProcessor processor;

  public DeathServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.processor =
        event -> {
          final var player = event.getEntity();
          final var deathLocation = player.getLocation().clone();
          for (final var drop : event.getDrops()) {
            deathLocation.getWorld().dropItemNaturally(deathLocation, drop);
          }

          final var gameService = ultraHardcore.gameService();
          gameService.sendMessage(
              prefix()
                  .append(text(player.getName(), NamedTextColor.GOLD))
                  .append(text(" est "))
                  .append(text("mort", NamedTextColor.RED))
                  .append(text('.'))
                  .build());

          gameService.playSound(
              Sound.sound()
                  .source(Sound.Source.HOSTILE)
                  .type(key("mob.wither.death"))
                  .volume(1f)
                  .pitch(1f)
                  .build());

          final var killer = player.getKiller();
          if (killer != null) {
            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            killer.giveExpLevels(3);
          }

          final var profile =
              ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
          if (profile != null) {
            profile.addComponent(
                new DeadComponent(
                    killer == null ? null : killer.getUniqueId(),
                    System.currentTimeMillis() - ultraHardcore.gameService().startTime()));
          }

          player.spigot().respawn();
          player.setGameMode(GameMode.SPECTATOR);
          player.teleport(new Location(ultraHardcore.worldService().getWorld(), 0.5, 200.5, 0.5));
        };
  }

  @Override
  public DeathProcessor getDeathProcessor() {
    return this.processor;
  }

  @Override
  public void installDeathProcessor(final DeathProcessor deathProcessor) {
    this.processor = deathProcessor;
  }
}
