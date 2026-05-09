package dev.kurai.uhc.command.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.menu.whitelist.WhitelistMenu;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.whitelist.service.WhitelistService;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@Command(@CommandMeta(name = "whitelist", aliases = "wl"))
@NullMarked
public final class WhitelistCommand {

  private final BukkitAudiences bukkitAudiences;
  private final WhitelistService whitelistService;

  public WhitelistCommand(
      final BukkitAudiences bukkitAudiences, final WhitelistService whitelistService) {
    this.bukkitAudiences = bukkitAudiences;
    this.whitelistService = whitelistService;
  }

  @SubCommand(@CommandMeta(name = "add", description = "Ajouter un joueur à la liste blanche."))
  public void add(final Player player, final @Argument(name = "joueurs") OfflinePlayer[] players) {
    final var message = text();

    for (var i = 0; i < players.length; i++) {
      final var target = players[i];
      if (i > 0) {
        message.appendNewline();
      }

      if (this.whitelistService.isWhitelisted(target.getUniqueId())) {
        message.append(
            text(CC.BURGER, NamedTextColor.GOLD).appendSpace().append(text(target.getName())));
        continue;
      }

      this.whitelistService.whitelist(
          player.getUniqueId(), target.getUniqueId(), "Whitelist Manuelle");
      message.append(
          text(CC.CHECKMARK, NamedTextColor.GREEN).appendSpace().append(text(target.getName())));
    }

    this.bukkitAudiences.player(player).sendMessage(message);
  }

  @SubCommand(@CommandMeta(name = "remove", description = "Retirer un joueur de la liste blanche."))
  public void remove(
      final Player player, final @Argument(name = "joueurs") OfflinePlayer[] players) {
    final var message = text();

    for (var i = 0; i < players.length; i++) {
      final var target = players[i];
      if (i > 0) {
        message.appendNewline();
      }

      if (!this.whitelistService.isWhitelisted(target.getUniqueId())) {
        message.append(
            text(CC.BURGER, NamedTextColor.GOLD).appendSpace().append(text(target.getName())));
        continue;
      }

      this.whitelistService.unwhitelist(target.getUniqueId());
      message.append(
          text(CC.CROSS, NamedTextColor.RED).appendSpace().append(text(target.getName())));
    }

    this.bukkitAudiences.player(player).sendMessage(message);
  }

  @SubCommand(@CommandMeta(name = "clear", description = "Vider la liste blanche."))
  public void clear(final Player player) {
    this.whitelistService.getWhitelistedPlayers().clear();
    this.bukkitAudiences
        .player(player)
        .sendMessage(
            prefix()
                .append(text("Vous avez "))
                .append(text("nettoyé", NamedTextColor.RED))
                .append(text(" la "))
                .append(text("liste blanche", NamedTextColor.AQUA))
                .append(text('.'))
                .build());
  }

  @SubCommand(@CommandMeta(name = "list", description = "Voir la liste blanche."))
  public void list(final Player player) {
    new WhitelistMenu(player, this.whitelistService).open();
  }
}
