package dev.kurai.uhc.command.argument;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public interface ArgumentResolver<T> {

    @Nullable T resolve(final CommandSender sender, final String argument);

    default Collection<String> complete(final CommandSender sender, final String argument) {
        return Collections.emptyList();
    }
}
