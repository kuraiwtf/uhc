package dev.kurai.uhc.game.death.service;

import dev.kurai.uhc.game.death.processor.DeathProcessor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DeathService {

  DeathProcessor getDeathProcessor();

  void installDeathProcessor(final DeathProcessor deathProcessor);
}
