package dev.kurai.uhc.command.argument.builtin.java;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public final class EnumArgumentResolver<E extends Enum<E>> implements ArgumentResolver<@Nullable E> {

    private final BukkitAudiences bukkitAudiences;
    private final Class<E> enumClass;

    public EnumArgumentResolver(final BukkitAudiences bukkitAudiences, final Class<E> enumClass) {
        this.bukkitAudiences = bukkitAudiences;
        this.enumClass = enumClass;
    }

    @Override
    public @Nullable E resolve(final CommandSender sender, final String argument) {
        for (final E constant : this.enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(argument)) {
                return constant;
            }
        }

        this.bukkitAudiences
                .sender(sender)
                .sendMessage(prefix().append(text("La valeur ", RED))
                        .append(text(argument, DARK_RED))
                        .append(text(" est invalide.", RED))
                        .build());
        return null;
    }

    @Override
    public Collection<String> complete(final CommandSender sender, final String argument) {
        final String input = argument.toLowerCase(Locale.ROOT);
        return java.util.Arrays.stream(this.enumClass.getEnumConstants())
                .map(constant -> constant.name().toLowerCase(Locale.ROOT))
                .filter(name -> name.startsWith(input))
                .sorted()
                .toList();
    }
}
