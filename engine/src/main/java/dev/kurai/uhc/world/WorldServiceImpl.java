package dev.kurai.uhc.world;

import com.google.common.collect.Sets;
import dev.kurai.uhc.profile.ProfileService;
import java.util.Set;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

public final class WorldServiceImpl implements WorldService {

  private final World world;

  private final Plugin plugin;
  private final ProfileService profileService;

  private final Set<String> preloadedWorlds;

  public WorldServiceImpl(final Plugin plugin, final ProfileService profileService) {
    this.plugin = plugin;
    this.profileService = profileService;

    this.world = WorldCreator.name("uhc_world").createWorld();
    this.world.setAutoSave(false);
    this.world.setGameRuleValue("doDaylightCycle", "false");
    this.world.setGameRuleValue("doWeatherCycle", "false");
    this.world.setGameRuleValue("doFireTick", "false");
    this.world.setGameRuleValue("naturalRegeneration", "false");
    this.world.setGameRuleValue("doTileDrops", "true");
    this.world.setSpawnLocation(0, 200, 0);

    this.preloadedWorlds = Sets.newHashSet();
  }

  @Override
  public World getWorld() {
    return this.world;
  }

  @Override
  public void preload(final World world, final int radius) {
    if (this.preloadedWorlds.add(world.getName())) {
      new WorldPreloadTask(this.profileService, world, radius).runTaskTimer(this.plugin, 0, 1L);
    }
  }
}
