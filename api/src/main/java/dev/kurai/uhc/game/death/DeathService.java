package dev.kurai.uhc.game.death;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DeathService {

  DeathProcessor getDeathProcessor();

  void installDeathProcessor(final DeathProcessor deathProcessor);
}
