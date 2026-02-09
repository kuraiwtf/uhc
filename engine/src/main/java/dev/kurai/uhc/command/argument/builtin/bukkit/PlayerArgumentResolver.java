package dev.kurai.uhc.command.argument.builtin.bukkit;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import java.util.Collection;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class PlayerArgumentResolver implements ArgumentResolver<@Nullable Player> {

  private final BukkitAudiences bukkitAudiences;

  public PlayerArgumentResolver(final @NotNull BukkitAudiences bukkitAudiences) {
    this.bukkitAudiences = bukkitAudiences;
  }

  @Override
  public @Nullable Player resolve(
      final @NotNull CommandSender sender, final @NotNull String argument) {
    final var found = Bukkit.getPlayer(argument);
    if (sender instanceof final Player player && argument.equalsIgnoreCase("self")) {
      return player;
    }

    if (found == null || !found.isOnline()) {
      this.bukkitAudiences
          .sender(sender)
          .sendMessage(
              prefix()
                  .append(text("Le joueur ", RED))
                  .append(text(argument, DARK_RED))
                  .append(text(" n'existe pas.", RED))
                  .build());
      return null;
    }

    return found;
  }

  @Override
  public @NotNull @Unmodifiable Collection<@NotNull String> complete(
      final @NotNull CommandSender sender, final @NotNull String argument) {
    return sender.getServer().getOnlinePlayers().stream()
        .map(Player::getName)
        .filter(s -> s.startsWith(argument))
        .sorted(String.CASE_INSENSITIVE_ORDER)
        .toList();
  }
}
