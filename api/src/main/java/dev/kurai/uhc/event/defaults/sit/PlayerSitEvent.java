package dev.kurai.uhc.event.defaults.sit;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerSitEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Player player;
  private long durationInTicks;
  private boolean cancelled;

  public PlayerSitEvent(final Player player, final long durationInTicks) {
    this.player = player;
    this.durationInTicks = durationInTicks;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public Player getPlayer() {
    return this.player;
  }

  public long getDurationInTicks() {
    return this.durationInTicks;
  }

  public void setDurationInTicks(final long durationInTicks) {
    this.durationInTicks = durationInTicks;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(final boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
