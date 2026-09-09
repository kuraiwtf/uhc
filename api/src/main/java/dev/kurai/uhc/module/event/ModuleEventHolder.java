package dev.kurai.uhc.module.event;

import java.util.Collection;
import org.jspecify.annotations.Nullable;

public interface ModuleEventHolder {

  Collection<ModuleEvent> events();

  void registerEvent(final ModuleEvent event);

  void unregisterEvent(final String identifier);

  @Nullable ModuleEvent event(final String identifier);
}
