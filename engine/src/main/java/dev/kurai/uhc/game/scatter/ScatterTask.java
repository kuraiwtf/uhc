package dev.kurai.uhc.game.scatter;

import com.google.common.collect.Lists;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class ScatterTask extends BukkitRunnable {

  private final GameService gameService;

  private final List<@NotNull UUID> players;
  private final List<@NotNull Location> positions;

  public ScatterTask(final @NotNull GameService gameService) {
    this.gameService = gameService;
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
    location.getChunk().load(true);

    final var player = Bukkit.getPlayer(found);
    player.teleport(location);
    player.sendMessage("Téléportation");
  }
}
