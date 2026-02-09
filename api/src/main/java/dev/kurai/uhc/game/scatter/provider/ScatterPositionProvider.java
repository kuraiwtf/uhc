package dev.kurai.uhc.game.scatter.provider;

import java.util.Collection;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ScatterPositionProvider {

  Collection<@NotNull Location> provideLocations(final int radius, final int players);
}
