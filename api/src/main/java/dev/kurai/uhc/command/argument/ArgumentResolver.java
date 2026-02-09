package dev.kurai.uhc.command.argument;

import java.util.Collection;
import java.util.Collections;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface ArgumentResolver<T> {

  T resolve(final @NotNull CommandSender sender, final @NotNull String argument);

  default Collection<@NotNull String> complete(
      final @NotNull CommandSender sender, final @NotNull String argument) {
    return Collections.emptyList();
  }
}
