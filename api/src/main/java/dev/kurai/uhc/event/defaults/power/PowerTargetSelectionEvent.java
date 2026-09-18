package dev.kurai.uhc.event.defaults.power;

import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.profile.Profile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PowerTargetSelectionEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile profile;
  private final Profile target;
  private final AbstractPower power;

  private boolean cancelled;

  public PowerTargetSelectionEvent(
      final Profile profile, final Profile target, final AbstractPower power) {
    this.profile = profile;
    this.target = target;
    this.power = power;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public Profile getProfile() {
    return this.profile;
  }

  public Profile getTarget() {
    return this.target;
  }

  public AbstractPower getPower() {
    return this.power;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(final boolean b) {
    this.cancelled = b;
  }
}
