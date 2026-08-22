package dev.kurai.uhc.game.scatter;

import java.util.Collection;
import org.bukkit.Location;

@FunctionalInterface
public interface ScatterPositionProvider {

  Collection<Location> provideLocations(final int radius, final int players);
}
