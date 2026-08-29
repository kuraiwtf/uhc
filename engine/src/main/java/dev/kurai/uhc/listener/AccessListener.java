package dev.kurai.uhc.listener;

import static dev.kurai.uhc.game.GameService.WHITELIST_OPTION;

import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.whitelist.WhitelistService;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public final class AccessListener implements Listener {

  private final HostService hostService;
  private final ModuleService moduleService;
  private final WhitelistService whitelistService;

  public AccessListener(
      final HostService hostService,
      final ModuleService moduleService,
      final WhitelistService whitelistService) {
    this.hostService = hostService;
    this.moduleService = moduleService;
    this.whitelistService = whitelistService;
  }

  @EventHandler
  public void onJoin(final AsyncPlayerPreLoginEvent event) {
    final UUID uniqueId = event.getUniqueId();
    if (WHITELIST_OPTION.getValue()
        && !this.whitelistService.isWhitelisted(uniqueId)
        && !this.hostService.hasHostAccess(uniqueId)
        && !Bukkit.getOfflinePlayer(uniqueId).isOp()) {
      event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
      event.setKickMessage("§cVous n'êtes pas dans la liste blanche de la partie.");
    }
  }
}
