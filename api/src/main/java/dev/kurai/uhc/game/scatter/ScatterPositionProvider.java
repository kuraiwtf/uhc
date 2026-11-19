package dev.kurai.uhc.game.scatter;

import org.bukkit.Location;

import java.util.Collection;

@FunctionalInterface
public interface ScatterPositionProvider {

    Collection<Location> provideLocations(final int radius, final int players);
}
