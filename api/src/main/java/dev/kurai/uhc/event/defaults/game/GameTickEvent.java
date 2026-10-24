package dev.kurai.uhc.event.defaults.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameTickEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private final int ticks;

  public GameTickEvent(final int ticks) {
    this.ticks = ticks;
  }

  public int ticks() {
    return this.ticks;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
