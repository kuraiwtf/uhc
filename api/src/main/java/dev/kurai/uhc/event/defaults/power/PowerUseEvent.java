package dev.kurai.uhc.event.defaults.power;

import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.profile.Profile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PowerUseEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile profile;
  private final AbstractPower power;

  private boolean cancelled;

  public PowerUseEvent(final @NotNull Profile profile, final @NotNull AbstractPower power) {
    this.profile = profile;
    this.power = power;
  }

  public @NotNull Profile getProfile() {
    return this.profile;
  }

  public @NotNull AbstractPower getPower() {
    return this.power;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
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
