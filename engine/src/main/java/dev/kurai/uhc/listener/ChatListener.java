package dev.kurai.uhc.listener;

import static dev.kurai.uhc.game.GameService.CHAT_OPTION;

import dev.kurai.uhc.game.GameService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatListener implements Listener {

  private final GameService gameService;

  public ChatListener(final GameService gameService) {
    this.gameService = gameService;
  }

  @EventHandler
  public void onChat(final AsyncPlayerChatEvent event) {
    if (CHAT_OPTION.getValue()) {
      return;
    }

    final Player player = event.getPlayer();
    final boolean bypass =
        this.gameService.startTime() == 0L && this.gameService.hostService().hasHostAccess(player);
    if (bypass) {
      return;
    }

    event.setCancelled(true);
  }
}
