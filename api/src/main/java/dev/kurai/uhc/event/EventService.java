package dev.kurai.uhc.event;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface EventService {

  void registerListener(final Listener listener);

  default void registerListeners(final Listener... listeners) {
    for (final var listener : listeners) {
      this.registerListener(listener);
    }
  }

  void unregisterListener(final Listener listener);

  default void unregisterListeners(final Listener... listeners) {
    for (final var listener : listeners) {
      this.unregisterListener(listener);
    }
  }

  <T extends Event> T dispatchEvent(final T event);
}
