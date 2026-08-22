package dev.kurai.uhc.game.death;

@FunctionalInterface
public interface DeathProcessor {

  void processDeath(final DeathContext context);
}
