package dev.kurai.uhc.world.service;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public interface WorldService {

  @NotNull
  World getWorld();

  void preload(final @NotNull World world, final int radius);
}
