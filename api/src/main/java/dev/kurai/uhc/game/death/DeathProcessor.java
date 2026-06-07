package dev.kurai.uhc.game.death;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DeathProcessor {

  void processDeath(final @NotNull DeathContext context);
}
