package dev.kurai.uhc.game.cycle;

import org.jetbrains.annotations.Range;

public interface CycleService extends CycleHolder {

  boolean enabled();

  void enabled(final boolean enabled);

  void start();

  int totalCycleDuration();

  void totalCycleDuration(final @Range(from = 5 * 60L, to = 30 * 60L) int duration);

  void skipCycleTo(AbstractCycle target);

  void skipCycle();
}
