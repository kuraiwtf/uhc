package dev.kurai.uhc.world;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class WorldServiceImpl implements WorldService {

  private final World world;

  private final Plugin plugin;
  private final BukkitAudiences bukkitAudiences;

  public WorldServiceImpl(
      final @NotNull Plugin plugin, final @NotNull BukkitAudiences bukkitAudiences) {
    this.plugin = plugin;
    this.bukkitAudiences = bukkitAudiences;

    this.world = WorldCreator.name("uhc_world").createWorld();
    this.world.setSpawnLocation(0, 200, 0);
  }

  @Override
  public @NotNull World getWorld() {
    return this.world;
  }

  @Override
  public void preload(final @NotNull World world, final int radius) {
    new WorldPreloadTask(this.bukkitAudiences, world, radius).runTaskTimer(this.plugin, 0, 1L);
  }
}
