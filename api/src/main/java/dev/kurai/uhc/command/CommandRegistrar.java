package dev.kurai.uhc.command;

import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.ArgumentResolverRegistrar;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CommandRegistrar {

  ArgumentResolverRegistrar getArgumentResolverRegistrar();

  PermissionProvider getPermissionProvider();

  void setPermissionProvider(final PermissionProvider permissionProvider);

  void registerCommand(final Object command);

  default void registerCommands(final Object... commands) {
    for (final var command : commands) {
      this.registerCommand(command);
    }
  }

  /**
   * Registers a single command built from several handlers: the {@code @SubCommand} and {@code
   * @DefaultCommand} methods of every handler are merged under {@code meta}.
   */
  void registerCommand(final CommandMeta meta, final Object... handlers);

  void unregisterCommand(final String name);

  default void unregisterCommands(final String... names) {
    for (final var name : names) {
      this.unregisterCommand(name);
    }
  }
}
