package dev.kurai.uhc.extension.mumble.listener;

import dev.kurai.uhc.event.defaults.game.GameStartEvent;
import dev.kurai.uhc.extension.mumble.MumbleExtension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class GameListener implements Listener {

  private final MumbleExtension extension;

  public GameListener(final MumbleExtension extension) {
    this.extension = extension;
  }

  @EventHandler
  public void onGameStart(final GameStartEvent event) {
    this.extension.start();
  }
}
