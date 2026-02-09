package dev.kurai.uhc.game.cycle.holder;

import dev.kurai.uhc.game.cycle.Cycle;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface CycleHolder {

  Collection<@NotNull Cycle> getPhases();

  void clearPhases();

  void registerCycle(final @NotNull Cycle cycle);

  default void registerCycles(final @NotNull Cycle @NotNull ... cycles) {
    for (final var cycle : cycles) {
      this.registerCycle(cycle);
    }
  }

  void registerCycle(final int index, final @NotNull Cycle cycle);

  void registerCycleBefore(final @NotNull Cycle from, final @NotNull Cycle cycle);

  void registerCycleAfter(final @NotNull Cycle from, final @NotNull Cycle cycle);

  void unregisterCycle(final @NotNull String id);

  default void unregisterCycles(final String @NotNull ... ids) {
    for (final var id : ids) {
      this.unregisterCycle(id);
    }
  }

  default void unregisterCycle(final @NotNull Cycle cycle) {
    this.unregisterCycle(cycle.getId());
  }

  default void unregisterPhases(final Cycle @NotNull ... cycles) {
    for (final var cycle : cycles) {
      this.unregisterCycle(cycle);
    }
  }

  boolean hasPhase(final @NotNull String id);
}
