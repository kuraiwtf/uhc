package dev.kurai.uhc.effect.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class StrengthApplyEvent extends Event implements Cancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final Player damager;
  private final Player victim;

  private double strengthValue;
  private boolean cancelled = false;

  public StrengthApplyEvent(Player damager, Player victim, double strengthValue) {
    this.damager = damager;
    this.victim = victim;
    this.strengthValue = strengthValue;
  }

  public Player damager() {
    return this.damager;
  }

  public Player victim() {
    return this.victim;
  }

  public double strengthValue() {
    return this.strengthValue;
  }

  public void strengthValue(final double resistanceValue) {
    this.strengthValue = resistanceValue;
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
