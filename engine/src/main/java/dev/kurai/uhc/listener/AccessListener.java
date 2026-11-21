package dev.kurai.uhc.listener;

import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.whitelist.WhitelistService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

import static dev.kurai.uhc.game.GameService.WHITELIST_OPTION;

public final class AccessListener implements Listener {

    private final HostService hostService;
    private final WhitelistService whitelistService;

    public AccessListener(final HostService hostService, final WhitelistService whitelistService) {
        this.hostService = hostService;
        this.whitelistService = whitelistService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(final PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL
                && this.hostService.hasHostAccess(event.getPlayer().getUniqueId())) {
            event.allow();
        }
    }

    @EventHandler
    public void onJoin(final AsyncPlayerPreLoginEvent event) {
        final UUID uniqueId = event.getUniqueId();
        if (WHITELIST_OPTION.getValue()
                && !this.whitelistService.isWhitelisted(uniqueId)
                && !this.hostService.hasHostAccess(uniqueId)) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
            event.setKickMessage("§cVous n'êtes pas dans la liste blanche de la partie.");
        }
    }
}
