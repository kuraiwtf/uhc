package dev.kurai.uhc.event.defaults.module;

import dev.kurai.uhc.module.AbstractModule;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ModuleInstallEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final AbstractModule previousModule;
  private final AbstractModule newModule;

  private boolean cancelled;

  public ModuleInstallEvent(final AbstractModule previousModule, final AbstractModule newModule) {
    this.previousModule = previousModule;
    this.newModule = newModule;
  }

  public AbstractModule getPreviousModule() {
    return this.previousModule;
  }

  public AbstractModule getNewModule() {
    return this.newModule;
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
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
