package dev.kurai.uhc.event.defaults.game;

import dev.kurai.uhc.game.cycle.AbstractCycle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
@Accessors(fluent = false)
@RequiredArgsConstructor
public final class CycleChangeEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final AbstractCycle oldCycle;
  private final AbstractCycle newCycle;

  private boolean cancelled;

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
