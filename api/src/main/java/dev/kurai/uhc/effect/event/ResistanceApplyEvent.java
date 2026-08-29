package dev.kurai.uhc.effect.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ResistanceApplyEvent extends Event implements Cancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final Player damager;
  private final Player victim;

  private double resistanceValue;
  private boolean cancelled = false;

  public ResistanceApplyEvent(Player damager, Player victim, double resistanceValue) {
    this.damager = damager;
    this.victim = victim;
    this.resistanceValue = resistanceValue;
  }

  public Player damager() {
    return this.damager;
  }

  public Player victim() {
    return this.victim;
  }

  public double resistanceValue() {
    return this.resistanceValue;
  }

  public void resistanceValue(final double resistanceValue) {
    this.resistanceValue = resistanceValue;
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
    return HANDLER_LIST;
  }
}
