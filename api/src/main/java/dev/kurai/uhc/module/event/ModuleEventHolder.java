package dev.kurai.uhc.module.event;

import java.util.Collection;
import org.bukkit.event.Event;
import org.jspecify.annotations.Nullable;

public interface ModuleEventHolder {

  Collection<ModuleEvent<?>> events();

  void registerEvent(final ModuleEvent<?> event);

  void unregisterEvent(final String identifier);

  <E extends Event> @Nullable ModuleEvent<E> event(final String identifier);
}
