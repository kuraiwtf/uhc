package dev.kurai.uhc.command.argument;

import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;

public interface ArgumentResolver<T> {

    T resolve(final CommandSender sender, final String argument);

    default Collection<String> complete(final CommandSender sender, final String argument) {
        return Collections.emptyList();
    }
}
