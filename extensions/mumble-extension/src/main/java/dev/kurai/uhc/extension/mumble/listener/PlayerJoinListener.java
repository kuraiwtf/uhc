package dev.kurai.uhc.extension.mumble.listener;

import dev.kurai.uhc.extension.mumble.MumbleExtension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

  private final MumbleExtension extension;

  public PlayerJoinListener(final MumbleExtension extension) {
    this.extension = extension;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    this.extension.advertise(event.getPlayer());
  }
}
