package dev.kurai.uhc.listener;

import static dev.kurai.uhc.game.GameService.WHITELIST_OPTION;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.minimessage.tag.Tag.inserting;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.whitelist.WhitelistService;
import java.util.UUID;
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
        && !this.hostService.hasHostAccess(uniqueId)) {
      event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
      event.kickMessage(
          miniMessage()
              .deserialize(
                  "<dark_gray>»</dark_gray> <gold><b><game_name></b></gold> <dark_gray>«</dark_gray><newline><newline>Vous avez été expulsé du serveur.<newline>Raison: <red><reason></red>",
                  resolver(
                      resolver(
                          "game_name",
                          inserting(
                              text(this.moduleService.getCurrentModule().getName(), GOLD, BOLD))),
                      resolver(
                          "reason",
                          inserting(text("Vous n'êtes pas dans la liste blanche.", RED))))));
    }
  }
}
