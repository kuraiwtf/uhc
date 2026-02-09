package dev.kurai.uhc.event.defaults.scenario;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CutCleanDropEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Player player;
  private final ItemStack item;
  private final int experience;
  private boolean cancelled;

  public CutCleanDropEvent(
      final @NotNull Player player, final @NotNull ItemStack item, final int experience) {
    this.player = player;
    this.item = item;
    this.experience = experience;
  }

  public @NotNull Player getPlayer() {
    return this.player;
  }

  public @NotNull ItemStack getItem() {
    return this.item;
  }

  public int getExperience() {
    return this.experience;
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
