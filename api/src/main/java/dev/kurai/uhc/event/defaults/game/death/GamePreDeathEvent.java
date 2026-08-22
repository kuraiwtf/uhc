package dev.kurai.uhc.event.defaults.game.death;

import dev.kurai.uhc.game.death.DeathContext;
import dev.kurai.uhc.profile.Profile;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class GamePreDeathEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Profile victim;
  private final @Nullable Profile killer;
  private final DeathContext context;

  private @Nullable Location respawnLocation;
  private boolean cancelled;

  public GamePreDeathEvent(
      final Profile victim, final @Nullable Profile killer, final DeathContext context) {
    this.victim = victim;
    this.killer = killer;
    this.context = context;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  public Profile getVictim() {
    return this.victim;
  }

  public @Nullable Profile getKiller() {
    return this.killer;
  }

  public DeathContext getContext() {
    return this.context;
  }

  public @Nullable Location getRespawnLocation() {
    return this.respawnLocation;
  }

  public void setRespawnLocation(final @Nullable Location respawnLocation) {
    this.respawnLocation = respawnLocation;
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
}
