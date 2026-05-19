package dev.kurai.uhc.game.cycle;

import org.jetbrains.annotations.Range;

public interface CycleService extends CycleHolder {

  boolean enabled();

  void enabled(final boolean enabled);

  int totalCycleDuration();

  void totalCycleDuration(final @Range(from = 5 * 60L, to = 20 * 60L) int duration);
}
