package dev.kurai.uhc.effect.event;

import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class EffectValidateEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final UUID uniqueId;

  public EffectValidateEvent(final UUID uniqueId) {
    this.uniqueId = uniqueId;
  }

  public UUID uniqueId() {
    return this.uniqueId;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
