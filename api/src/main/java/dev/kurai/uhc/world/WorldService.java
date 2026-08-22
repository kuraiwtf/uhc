package dev.kurai.uhc.world;

import org.bukkit.World;

public interface WorldService {

  World getWorld();

  void preload(final World world, final int radius);
}
