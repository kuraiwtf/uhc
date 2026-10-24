package dev.kurai.uhc.event.defaults.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameSecondEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private final int seconds;

  public GameSecondEvent(final int seconds) {
    this.seconds = seconds;
  }

  public int ticks() {
    return this.seconds;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
