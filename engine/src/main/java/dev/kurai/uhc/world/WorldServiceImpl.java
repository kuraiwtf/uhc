package dev.kurai.uhc.world;

import dev.kurai.uhc.profile.ProfileService;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

public final class WorldServiceImpl implements WorldService {

  private final World world;

  private final Plugin plugin;
  private final ProfileService profileService;

  public WorldServiceImpl(final Plugin plugin, final ProfileService profileService) {
    this.plugin = plugin;
    this.profileService = profileService;

    this.world = WorldCreator.name("uhc_world").createWorld();
    this.world.setSpawnLocation(0, 200, 0);
  }

  @Override
  public World getWorld() {
    return this.world;
  }

  @Override
  public void preload(final World world, final int radius) {
    new WorldPreloadTask(this.profileService, world, radius).runTaskTimer(this.plugin, 0, 1L);
  }
}
