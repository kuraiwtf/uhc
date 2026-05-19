package dev.kurai.uhc.event.defaults.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameEpisodeChangeEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final int episode;

  public GameEpisodeChangeEvent(final int episode) {
    this.episode = episode;
  }

  public int getEpisode() {
    return this.episode;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
