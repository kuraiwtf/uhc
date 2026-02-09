package dev.kurai.uhc.game.start.phase.holder;

import dev.kurai.uhc.game.start.phase.StartPhase;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface StartPhaseHolder {

  Collection<@NotNull StartPhase> getPhases();

  void clearPhases();

  void registerPhase(final @NotNull StartPhase phase);

  default void registerPhases(final @NotNull StartPhase @NotNull ... phases) {
    for (final var phase : phases) {
      this.registerPhase(phase);
    }
  }

  void registerPhase(final int index, final @NotNull StartPhase phase);

  void registerPhaseBefore(final @NotNull StartPhase from, final @NotNull StartPhase phase);

  void registerPhaseAfter(final @NotNull StartPhase from, final @NotNull StartPhase phase);

  void unregisterPhase(final @NotNull String id);

  default void unregisterPhases(final String @NotNull ... ids) {
    for (final var id : ids) {
      this.unregisterPhase(id);
    }
  }

  default void unregisterPhase(final @NotNull StartPhase phase) {
    this.unregisterPhase(phase.getId());
  }

  default void unregisterPhases(final StartPhase @NotNull ... phases) {
    for (final var phase : phases) {
      this.unregisterPhase(phase);
    }
  }

  boolean hasPhase(final @NotNull String id);
}
