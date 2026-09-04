package dev.kurai.uhc.command.defaults;

import static dev.kurai.uhc.util.CC.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.menu.whitelist.WhitelistMenu;
import dev.kurai.uhc.whitelist.WhitelistService;
import dev.kurai.uhc.whitelist.hostmc.HostMCWhitelistProviderTask;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@Command(@CommandMeta(name = "whitelist", aliases = "wl", permission = "uhc.command.whitelist"))
@NullMarked
@RequiredArgsConstructor
public final class WhitelistCommand {

  private static final Pattern MATCHER = Pattern.compile("[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}");

  private final UltraHardcoreAPI ultraHardcore;

  @SubCommand(
      @CommandMeta(
          name = "add",
          description = "Ajouter un joueur à la liste blanche.",
          permission = "uhc.command.whitelist.add"))
  public void add(
      final CommandSender sender, final @Argument(name = "joueurs") OfflinePlayer[] players) {
    final WhitelistService whitelistService = this.ultraHardcore.whitelistService();
    for (final OfflinePlayer target : players) {
      if (whitelistService.isWhitelisted(target.getUniqueId())) {
        sender.sendMessage("§6" + BURGER + " " + target.getName());
        continue;
      }

      whitelistService.whitelist(
          sender instanceof final Player player ? player.getUniqueId() : new UUID(0, 0),
          target.getUniqueId(),
          "Whitelist Manuelle");
      sender.sendMessage("§a" + CHECKMARK + " " + target.getName());
    }
  }

  @SubCommand(
      @CommandMeta(
          name = "remove",
          description = "Retirer un joueur de la liste blanche.",
          permission = "uhc.command.whitelist.remove"))
  public void remove(
      final CommandSender sender, final @Argument(name = "joueurs") OfflinePlayer[] players) {
    final WhitelistService whitelistService = this.ultraHardcore.whitelistService();
    for (final OfflinePlayer target : players) {
      if (!whitelistService.isWhitelisted(target.getUniqueId())) {
        sender.sendMessage("§6" + BURGER + " " + target.getName());
        continue;
      }

      whitelistService.unwhitelist(target.getUniqueId());
      sender.sendMessage("§c" + CROSS + " " + target.getName());
    }
  }

  @SubCommand(
      @CommandMeta(
          name = "clear",
          description = "Vider la liste blanche.",
          permission = "uhc.command.whitelist.clear"))
  public void clear(final CommandSender sender) {
    this.ultraHardcore.whitelistService().getWhitelistedPlayers().clear();
    sender.sendMessage(prefix("La&b liste blanche&f a bien été&d nettoyée&r."));
  }

  @SubCommand(
      @CommandMeta(
          name = "list",
          description = "Voir la liste blanche.",
          permission = "uhc.command.whitelist.list"))
  public void list(final Player player) {
    new WhitelistMenu(player, this.ultraHardcore.whitelistService()).open();
  }

  @SubCommand(
      @CommandMeta(
          name = "setup",
          description = "Utiliser la liste blanche du bot discord d'Host MC.",
          permission = "uhc.command.whitelist.setup"))
  public void setup(final Player player, final @Argument(name = "code") String code) {
    if (!MATCHER.matcher(code).find()) {
      player.sendMessage(prefix("Le code&b %s&r est&c invalide&r.".formatted(code)));
      return;
    }

    final Plugin plugin = this.ultraHardcore.plugin();
    new HostMCWhitelistProviderTask(
            this.ultraHardcore.whitelistService(),
            plugin.getConfig().getString("authorization"),
            code)
        .runTaskTimer(plugin, 0L, 10 * 20L);

    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
    player.sendMessage(
        prefix(
            "Tentative de&d liaison&r avec le système du&6 bot discord d'Host MC&r en cours..."));
  }
}
