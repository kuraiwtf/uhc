package dev.kurai.uhc.item;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomItem {

  private final String identifier;
  private final boolean hostOnly;
  private final boolean spectatorOnly;

  public CustomItem(final String identifier, final boolean hostOnly, final boolean spectatorOnly) {
    this.identifier = identifier;
    this.hostOnly = hostOnly;
    this.spectatorOnly = spectatorOnly;
  }

  public abstract ItemStack provideIcon(final Player player);

  public void onInteract(final Player player, final PlayerInteractEvent event) {}

  public void onInventoryClick(final Player player, final InventoryClickEvent event) {}

  public void onDrop(final Player player, final PlayerDropItemEvent event) {}

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
