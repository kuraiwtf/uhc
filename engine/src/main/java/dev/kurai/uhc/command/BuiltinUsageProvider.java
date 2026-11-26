package dev.kurai.uhc.command;

import dev.kurai.uhc.command.argument.data.ArgumentData;
import net.kyori.adventure.text.Component;

import java.util.List;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public final class BuiltinUsageProvider implements UsageProvider {

    @Override
    public Component getUsage(final String command, final List<ArgumentData> arguments) {
        final var usage = new StringBuilder("/").append(command);
        for (final var argument : arguments) {
            usage.append(' ');
            if (argument.defaultValue().isEmpty()) {
                usage.append('<').append(argument.name()).append('>');
            } else {
                usage.append('[').append(argument.name()).append(']');
            }
        }

        return prefix().append(text("Utilisation: " + usage, RED)).build();
    }
}
