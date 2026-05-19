package dev.kurai.uhc.game.scatter;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.GameService;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class ScatterServiceImpl implements ScatterService {

  private final GameService gameService;
  private final Plugin plugin;
  private final BukkitAudiences bukkitAudiences;

  private ScatterPositionProvider positionProvider;

  public ScatterServiceImpl(
      final @NotNull UltraHardcoreAPI ultraHardcore, final @NotNull GameService gameService) {
    this.gameService = gameService;
    this.plugin = ultraHardcore.getPlugin();
    this.bukkitAudiences = ultraHardcore.getBukkitAudiences();

    this.positionProvider =
        (radius, players) -> {
          final var locations = Lists.<Location>newArrayListWithCapacity(players);

          final var world = ultraHardcore.getWorldService().getWorld();
          final var center = world.getSpawnLocation();

          final double angleStep = (2 * Math.PI) / players;

          for (int i = 0; i < players; i++) {
            final double angle = angleStep * i;

            final int x = (int) (center.getX() + radius * Math.cos(angle));
            final int z = (int) (center.getZ() + radius * Math.sin(angle));

            locations.add(world.getHighestBlockAt(x, z).getLocation());
          }

          return locations;
        };
  }

  @Override
  public @NotNull ScatterPositionProvider getPositionProvider() {
    return this.positionProvider;
  }

  @Override
  public void setPositionProvider(final @NotNull ScatterPositionProvider positionProvider) {
    this.positionProvider = positionProvider;
  }

  @Override
  public void handleScatter() {
    new ScatterTask(this.gameService, this.bukkitAudiences).runTaskTimer(this.plugin, 0, 1L);
  }

  @Override
  public void handlePlayerLateScatter(final @NotNull Player player) {}
}
