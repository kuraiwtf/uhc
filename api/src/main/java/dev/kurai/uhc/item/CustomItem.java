package dev.kurai.uhc.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomItem {

  private final String identifier;
  private final boolean hostOnly;
  private final boolean spectatorOnly;

  public CustomItem(final String identifier, final boolean hostOnly, final boolean spectatorOnly) {
    this.identifier = identifier;
    this.hostOnly = hostOnly;
    this.spectatorOnly = spectatorOnly;
  }

  public abstract @NotNull ItemStack provideIcon(final @NotNull Player player);

  public void onInteract(final @NotNull Player player, final @NotNull PlayerInteractEvent event) {}

  public void onInventoryClick(
      final @NotNull Player player, final @NotNull InventoryClickEvent event) {}

  public void onDrop(final @NotNull Player player, final @NotNull PlayerDropItemEvent event) {}

  public final String getIdentifier() {
    return this.identifier;
  }

  public final boolean isHostOnly() {
    return this.hostOnly;
  }

  public final boolean isSpectatorOnly() {
    return this.spectatorOnly;
  }
}
