package dev.kurai.uhc.event.defaults.host;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
@Setter
@Accessors(fluent = false)
public final class HostAccessUpdateEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final UUID id;
  private final Type type;
  private final Status status;

  private boolean cancelled;

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public enum Type {
    HOST,
    CO_HOST
  }

  public enum Status {
    ALLOWED,
    DENIED
  }
}
