package dev.kurai.uhc.command.argument.builtin.bukkit;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import java.util.Collection;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public final class WorldArgumentResolver implements ArgumentResolver<@Nullable World> {

  private final BukkitAudiences bukkitAudiences;

  public WorldArgumentResolver(final BukkitAudiences bukkitAudiences) {
    this.bukkitAudiences = bukkitAudiences;
  }

  @Override
  public @Nullable World resolve(final CommandSender sender, final String argument) {
    final var world = Bukkit.getWorld(argument);
    if (world == null) {
      this.bukkitAudiences
          .sender(sender)
          .sendMessage(
              prefix()
                  .append(text("Le monde ", RED))
                  .append(text(argument, DARK_RED))
                  .append(text(" n'existe pas.", RED))
                  .build());
      return null;
    }

    return world;
  }

  @Override
  public @Unmodifiable Collection<String> complete(
      final CommandSender sender, final String argument) {
    return Bukkit.getWorlds().stream()
        .map(World::getName)
        .map(String::toLowerCase)
        .filter(s -> s.startsWith(argument.toLowerCase()))
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList();
  }
}
