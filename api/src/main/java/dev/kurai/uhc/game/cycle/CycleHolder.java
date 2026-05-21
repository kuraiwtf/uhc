package dev.kurai.uhc.game.cycle;

import java.util.Collection;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public interface CycleHolder {

  Collection<Cycle> getPhases();

  void clearPhases();

  void registerCycle(final Cycle cycle);

  default void registerCycles(final Cycle... cycles) {
    for (final var cycle : cycles) {
      this.registerCycle(cycle);
    }
  }

  void registerCycle(final int index, final Cycle cycle);

  void registerCycleBefore(final Cycle from, final Cycle cycle);

  void registerCycleAfter(final Cycle from, final Cycle cycle);

  void unregisterCycle(final String id);

  default void unregisterCycles(final String... ids) {
    for (final var id : ids) {
      this.unregisterCycle(id);
    }
  }

  default void unregisterCycle(final Cycle cycle) {
    this.unregisterCycle(cycle.getId());
  }

  default void unregisterCycles(final Cycle... cycles) {
    for (final var cycle : cycles) {
      this.unregisterCycle(cycle);
    }
  }

  boolean hasPhase(final String id);
}
