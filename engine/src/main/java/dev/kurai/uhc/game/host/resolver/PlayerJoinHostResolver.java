package dev.kurai.uhc.game.host.resolver;

import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.game.host.TickableHostResolver;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.Nullable;

public final class PlayerJoinHostResolver implements TickableHostResolver, Listener {

  private final EventService eventService;
  private @Nullable UUID hostFound;

  public PlayerJoinHostResolver(final EventService eventService) {
    this.eventService = eventService;
    this.eventService.registerListener(this);
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    if (!event.getPlayer().isOp()) {
      return;
    }

    this.hostFound = event.getPlayer().getUniqueId();
    this.eventService.unregisterListener(this);
  }

  @Override
  public @Nullable UUID resolveHost() {
    return this.hostFound;
  }
}
