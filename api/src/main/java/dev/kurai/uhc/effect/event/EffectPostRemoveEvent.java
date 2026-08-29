package dev.kurai.uhc.effect.event;

import dev.kurai.uhc.effect.Effect;
import java.util.UUID;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class EffectPostRemoveEvent extends Event implements Cancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final UUID uniqueId;
  private final Effect effect;

  private boolean cancelled = false;

  public EffectPostRemoveEvent(final UUID uniqueId, final Effect effect) {
    this.uniqueId = uniqueId;
    this.effect = effect;
  }

  public UUID uniqueId() {
    return this.uniqueId;
  }

  public Effect effect() {
    return this.effect;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(final boolean b) {
    this.cancelled = b;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
