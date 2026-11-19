package dev.kurai.uhc.module.event;

import org.jspecify.annotations.Nullable;

import java.util.Collection;

public interface ModuleEventHolder {

    Collection<ModuleEvent> events();

    void registerEvent(final ModuleEvent event);

    void unregisterEvent(final String identifier);

    @Nullable
    ModuleEvent event(final String identifier);
}
