package dev.kurai.uhc.module.command;

import dev.kurai.uhc.module.AbstractModule;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Contributes sub-commands to the command of an installed module (e.g. {@code /<module> role}). The
 * returned handler is an object whose methods are annotated with {@code @SubCommand} (and
 * optionally {@code @DefaultCommand}).
 */
@NullMarked
public interface ModuleCommandProvider {

    /** Returns the handler for this module, or {@code null} if the module is not concerned. */
    @Nullable
    Object provide(final AbstractModule module);
}
