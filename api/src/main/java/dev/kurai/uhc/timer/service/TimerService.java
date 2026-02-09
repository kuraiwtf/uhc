package dev.kurai.uhc.timer.service;

import dev.kurai.uhc.timer.AbstractTimer;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface TimerService {

  void registerTimer(final @NotNull AbstractTimer timer);

  default void registerTimers(final AbstractTimer @NotNull ... timers) {
    for (final var timer : timers) {
      this.registerTimer(timer);
    }
  }

  void unregisterTimer(final @NotNull String identifier);

  default void unregisterTimers(final String @NotNull ... identifiers) {
    for (final var identifier : identifiers) {
      this.unregisterTimer(identifier);
    }
  }

  @NotNull
  Optional<AbstractTimer> getTimer(final @NotNull String identifier);

  @NotNull
  Collection<AbstractTimer> getTimers();

  void startAllTimers();

  void stopAllTimers();
}
