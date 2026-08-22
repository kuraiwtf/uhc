package dev.kurai.uhc.command;

import dev.kurai.uhc.command.argument.ArgumentResolverRegistrar;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CommandRegistrar {

  ArgumentResolverRegistrar getArgumentResolverRegistrar();

  void registerCommand(final Object command);

  default void registerCommands(final Object... commands) {
    for (final var command : commands) {
      this.registerCommand(command);
    }
  }

  void unregisterCommand(final String name);

  default void unregisterCommands(final String... names) {
    for (final var name : names) {
      this.unregisterCommand(name);
    }
  }
}
