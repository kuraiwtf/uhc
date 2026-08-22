package dev.kurai.uhc.game.cycle;

import java.util.Collection;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public interface CycleHolder {

  Collection<AbstractCycle> getPhases();

  void clearPhases();

  void registerCycle(final AbstractCycle cycle);

  default void registerCycles(final AbstractCycle... cycles) {
    for (final var cycle : cycles) {
      this.registerCycle(cycle);
    }
  }

  void registerCycle(final int index, final AbstractCycle cycle);

  void registerCycleBefore(final AbstractCycle from, final AbstractCycle cycle);

  void registerCycleAfter(final AbstractCycle from, final AbstractCycle cycle);

  void unregisterCycle(final String id);

  default void unregisterCycles(final String... ids) {
    for (final var id : ids) {
      this.unregisterCycle(id);
    }
  }

  default void unregisterCycle(final AbstractCycle cycle) {
    this.unregisterCycle(cycle.getId());
  }

  default void unregisterCycles(final AbstractCycle... cycles) {
    for (final var cycle : cycles) {
      this.unregisterCycle(cycle);
    }
  }

  boolean hasPhase(final String id);
}
