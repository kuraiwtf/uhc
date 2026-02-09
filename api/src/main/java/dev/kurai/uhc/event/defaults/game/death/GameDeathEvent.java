package dev.kurai.uhc.event.defaults.game.death;

import dev.kurai.uhc.profile.Profile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class GameDeathEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final @Nullable Profile killer;
  private final Profile victim;

  public GameDeathEvent(final @Nullable Profile killer, final Profile victim) {
    this.killer = killer;
    this.victim = victim;
  }

  public @Nullable Profile getKiller() {
    return this.killer;
  }

  public Profile getVictim() {
    return this.victim;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
