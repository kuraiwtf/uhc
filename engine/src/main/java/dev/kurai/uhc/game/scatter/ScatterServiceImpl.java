package dev.kurai.uhc.game.scatter;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ScatterServiceImpl implements ScatterService {

  private final UltraHardcoreAPI ultraHardcore;

  private ScatterPositionProvider positionProvider;

  public ScatterServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;

    this.positionProvider =
        (radius, players) -> {
          final var locations = Lists.<Location>newArrayListWithCapacity(players);

          final var world = ultraHardcore.worldService().getWorld();
          final var center = world.getSpawnLocation();

          final double angleStep = (2 * Math.PI) / players;

          for (int i = 0; i < players; i++) {
            final double angle = angleStep * i;

            final int x = (int) (center.getX() + radius * Math.cos(angle));
            final int z = (int) (center.getZ() + radius * Math.sin(angle));

            locations.add(world.getHighestBlockAt(x, z).getLocation().add(0, 1, 0));
          }

          return locations;
        };
  }

  @Override
  public ScatterPositionProvider getPositionProvider() {
    return this.positionProvider;
  }

  @Override
  public void setPositionProvider(final ScatterPositionProvider positionProvider) {
    this.positionProvider = positionProvider;
  }

  @Override
  public void handleScatter() {
    new ScatterTask(this.ultraHardcore).runTaskTimer(this.ultraHardcore.plugin(), 0, 1L);
  }

  @Override
  public void handlePlayerLateScatter(final Player player) {}
}
