package dev.kurai.uhc.game.start.phase.holder;

import dev.kurai.uhc.game.start.phase.StartPhase;
import java.util.Collection;

public interface StartPhaseHolder {

  Collection<StartPhase> getPhases();

  void clearPhases();

  void registerPhase(final StartPhase phase);

  default void registerPhases(final StartPhase... phases) {
    for (final var phase : phases) {
      this.registerPhase(phase);
    }
  }

  void registerPhase(final int index, final StartPhase phase);

  void registerPhaseBefore(final StartPhase from, final StartPhase phase);

  void registerPhaseAfter(final StartPhase from, final StartPhase phase);

  void unregisterPhase(final String id);

  default void unregisterPhases(final String... ids) {
    for (final var id : ids) {
      this.unregisterPhase(id);
    }
  }

  default void unregisterPhase(final StartPhase phase) {
    this.unregisterPhase(phase.getId());
  }

  default void unregisterPhases(final StartPhase... phases) {
    for (final var phase : phases) {
      this.unregisterPhase(phase);
    }
  }

  boolean hasPhase(final String id);
}
