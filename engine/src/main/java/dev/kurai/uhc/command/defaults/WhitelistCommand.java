package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.menu.whitelist.WhitelistMenu;
import dev.kurai.uhc.whitelist.WhitelistService;
import dev.kurai.uhc.whitelist.hostmc.HostMCWhitelistProviderTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.regex.Pattern;

import static dev.kurai.uhc.util.CC.*;

@Command(@CommandMeta(name = "whitelist", aliases = "wl"))
@RequiredArgsConstructor
public final class WhitelistCommand {

    private static final Pattern MATCHER = Pattern.compile("[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}");

    private final UltraHardcoreAPI ultraHardcore;

    private @Nullable BukkitTask hostMcTask;

    @SubCommand(@CommandMeta(name = "add", description = "Ajouter un joueur à la liste blanche."))
    public void add(final CommandSender sender, final @Argument(name = "joueurs") OfflinePlayer[] players) {
        if (sender instanceof final Player player) {
            if (!this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
                player.sendMessage(MISSING_PERMISSION);
                return;
            }
        }

        final WhitelistService whitelistService = this.ultraHardcore.whitelistService();
        for (final OfflinePlayer target : players) {
            if (whitelistService.whitelisted(target.getUniqueId())) {
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

    @SubCommand(@CommandMeta(name = "remove", description = "Retirer un joueur de la liste blanche."))
    public void remove(final CommandSender sender, final @Argument(name = "joueurs") OfflinePlayer[] players) {
        if (sender instanceof final Player player) {
            if (!this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
                player.sendMessage(MISSING_PERMISSION);
                return;
            }
        }

        final WhitelistService whitelistService = this.ultraHardcore.whitelistService();
        for (final OfflinePlayer target : players) {
            if (!whitelistService.whitelisted(target.getUniqueId())) {
                sender.sendMessage("§6" + BURGER + " " + target.getName());
                continue;
            }

            whitelistService.unwhitelist(target.getUniqueId());
            sender.sendMessage("§c" + CROSS + " " + target.getName());
        }
    }

    @SubCommand(@CommandMeta(name = "clear", description = "Vider la liste blanche."))
    public void clear(final CommandSender sender) {
        if (sender instanceof final Player player) {
            if (!this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
                player.sendMessage(MISSING_PERMISSION);
                return;
            }
        }

        this.ultraHardcore.whitelistService().getWhitelistedPlayers().clear();
        sender.sendMessage(prefix("La&b liste blanche&f a bien été&d nettoyée&r."));
    }

    @SubCommand(@CommandMeta(name = "list", description = "Voir la liste blanche."))
    public void list(final Player player) {
        if (!this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
            player.sendMessage(MISSING_PERMISSION);
            return;
        }

        new WhitelistMenu(player, this.ultraHardcore.whitelistService()).open();
    }

    @SubCommand(@CommandMeta(name = "setup", description = "Utiliser la liste blanche du bot discord d'Host MC."))
    public void setup(final Player player, final @Argument(name = "code") String code) {
        if (!this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
            player.sendMessage(MISSING_PERMISSION);
            return;
        }

        if (!MATCHER.matcher(code).matches()) {
            player.sendMessage(prefix("Le code&b %s&r est&c invalide&r.".formatted(code)));
            return;
        }

        final Plugin plugin = this.ultraHardcore.plugin();
        final String authorization = plugin.getConfig().getString("authorization", "");
        if (this.hostMcTask != null) {
            this.hostMcTask.cancel();
        }

        this.hostMcTask = new HostMCWhitelistProviderTask(
                        this.ultraHardcore, this.ultraHardcore.whitelistService(), authorization, code)
                .runTaskTimerAsynchronously(plugin, 0L, 10 * 20L);

        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
        player.sendMessage(prefix("Tentative de&d liaison&r avec le système du&6 bot discord d'Host MC&r en cours..."));
    }
}
