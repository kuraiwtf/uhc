package dev.kurai.uhc.command.argument.builtin.bukkit;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import java.util.Collection;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class OfflinePlayerArgumentResolver
    implements ArgumentResolver<@Nullable OfflinePlayer> {

  private final BukkitAudiences bukkitAudiences;

  public OfflinePlayerArgumentResolver(final @NotNull BukkitAudiences bukkitAudiences) {
    this.bukkitAudiences = bukkitAudiences;
  }

  @Override
  public @Nullable OfflinePlayer resolve(
      final @NotNull CommandSender sender, final @NotNull String argument) {
    final var found = Bukkit.getOfflinePlayer(argument);
    if (sender instanceof final OfflinePlayer player && argument.equalsIgnoreCase("self")) {
      return player;
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
