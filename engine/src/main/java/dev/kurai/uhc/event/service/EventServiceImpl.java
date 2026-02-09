package dev.kurai.uhc.event.service;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class EventServiceImpl implements EventService {

  private final Plugin plugin;

  public EventServiceImpl(final Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void registerListener(final Listener listener) {
    this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
  }

  @Override
  public void unregisterListener(final Listener listener) {
    HandlerList.unregisterAll(listener);
  }

  @Override
  public <T extends Event> T dispatchEvent(final T event) {
    this.plugin.getServer().getPluginManager().callEvent(event);
    return event;
  }
}
