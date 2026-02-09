package dev.kurai.uhc.command.registrar;

import dev.kurai.uhc.command.argument.registrar.ArgumentResolverRegistrar;
import org.jetbrains.annotations.NotNull;

public interface CommandRegistrar {

  @NotNull
  ArgumentResolverRegistrar getArgumentResolverRegistrar();

  void registerCommand(final @NotNull Object command);

  default void registerCommands(final Object @NotNull ... commands) {
    for (final var command : commands) {
      this.registerCommand(command);
    }
  }

  void unregisterCommand(final @NotNull String name);

  default void unregisterCommands(final String @NotNull ... names) {
    for (final var name : names) {
      this.unregisterCommand(name);
    }
  }
}
