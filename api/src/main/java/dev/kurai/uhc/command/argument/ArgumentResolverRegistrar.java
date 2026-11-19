package dev.kurai.uhc.command.argument;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

@NullMarked
public interface ArgumentResolverRegistrar {

    void registerArgumentResolver(final Class<?> clazz, final ArgumentResolver<?> resolver);

    <T> @Nullable T resolveArgument(final Class<?> clazz, final CommandSender sender, final String argument);

    Collection<String> complete(final Class<?> clazz, final CommandSender sender, final String argument);
}
