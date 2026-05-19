package dev.kurai.uhc.command.argument;

import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface ArgumentResolverRegistrar {

  void registerArgumentResolver(
      final @NotNull Class<?> clazz, final @NotNull ArgumentResolver<?> resolver);

  <T> T resolveArgument(
      final @NotNull Class<?> clazz,
      final @NotNull CommandSender sender,
      final @NotNull String argument);

  Collection<@NotNull String> complete(
      final @NotNull Class<?> clazz,
      final @NotNull CommandSender sender,
      final @NotNull String argument);
}
