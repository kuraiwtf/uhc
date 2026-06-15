package dev.kurai.uhc.command.argument;

import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ArgumentResolverRegistrar {

  void registerArgumentResolver(final Class<?> clazz, final ArgumentResolver<?> resolver);

  <T> T resolveArgument(final Class<?> clazz, final CommandSender sender, final String argument);

  Collection<String> complete(
      final Class<?> clazz, final CommandSender sender, final String argument);
}
