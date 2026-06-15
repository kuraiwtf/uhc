package dev.kurai.uhc.timer;

import java.util.Collection;
import java.util.Optional;

public interface TimerService {

  void registerTimer(final AbstractTimer timer);

  default void registerTimers(final AbstractTimer... timers) {
    for (final var timer : timers) {
      this.registerTimer(timer);
    }
  }

  void unregisterTimer(final String identifier);

  default void unregisterTimers(final String... identifiers) {
    for (final var identifier : identifiers) {
      this.unregisterTimer(identifier);
    }
  }

  Optional<AbstractTimer> getTimer(final String identifier);

  Collection<AbstractTimer> getTimers();

  void startAllTimers();

  void stopAllTimers();
}
