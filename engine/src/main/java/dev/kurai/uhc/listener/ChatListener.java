package dev.kurai.uhc.listener;

import static dev.kurai.uhc.game.GameService.CHAT_OPTION;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class ChatListener implements Listener {

  @EventHandler
  public void onChat(final AsyncPlayerChatEvent event) {
    if (!CHAT_OPTION.getValue()) {
      event.setCancelled(true);
    }
  }
}
