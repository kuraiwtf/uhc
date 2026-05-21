package dev.kurai.uhc.game.scatter;

import static net.kyori.adventure.text.Component.text;

import com.google.common.collect.Lists;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class ScatterTask extends BukkitRunnable {

  private final GameService gameService;
  private final List<@NotNull UUID> players;
  private final List<@NotNull Location> positions;
  private final BukkitAudiences bukkitAudiences;

  public ScatterTask(
      final @NotNull GameService gameService, final @NotNull BukkitAudiences bukkitAudiences) {
    this.gameService = gameService;
    this.bukkitAudiences = bukkitAudiences;
    this.players =
        Lists.newArrayList(Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).toList());
    this.positions =
        Lists.newArrayList(
            gameService
                .scatterService()
                .getPositionProvider()
                .provideLocations(
                    BorderConfiguration.INITIAL_SIZE_OPTION.getValue() - 50, this.players.size()));
  }

  @Override
  public void run() {
    if (this.players.isEmpty()) {
      this.gameService.startService().handleFinalStart();
      this.cancel();
      return;
    }

    final var found = this.players.removeFirst();
    final var location = this.positions.removeFirst();
    Bukkit.getPlayer(found).teleport(location);
    this.bukkitAudiences.player(found).sendMessage(text("Téléportation"));
  }
}
