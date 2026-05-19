package dev.kurai.uhc.listener;

import dev.kurai.uhc.item.ItemService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class ItemListener implements Listener {

  private final ItemService itemService;

  public ItemListener(final @NotNull ItemService itemService) {
    this.itemService = itemService;
  }

  @EventHandler
  public void onInteract(final @NotNull PlayerInteractEvent event) {
    final var player = event.getPlayer();
    this.itemService
        .findByIcon(player, event.getItem())
        .ifPresent(customItem -> customItem.onInteract(player, event));
  }

  @EventHandler
  public void onDrop(final @NotNull PlayerDropItemEvent event) {
    final var player = event.getPlayer();
    this.itemService
        .findByIcon(player, event.getItemDrop().getItemStack())
        .ifPresent(customItem -> customItem.onDrop(player, event));
  }

  @EventHandler
  public void onClick(final @NotNull InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof final Player player)) {
      return;
    }

    final var item = event.getCurrentItem();
    if (item == null) {
      return;
    }

    this.itemService
        .findByIcon(player, item)
        .ifPresent(customItem -> customItem.onInventoryClick(player, event));
  }
}
