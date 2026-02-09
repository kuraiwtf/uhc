package dev.kurai.uhc.item.builtin;

import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class LogoutItem extends CustomItem {

  private static final String IDENTIFIER = "logout";

  public LogoutItem() {
    super(IDENTIFIER, false, false);
  }

  @Override
  public @NotNull ItemStack provideIcon(final @NotNull Player player) {
    return new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
        .name("&c&lQuitter&8 " + CC.SQUARE + " &7Clic-Droit")
        .lore("", "&7" + CC.BAR + "&f Permet de quitter la partie.", "")
        .glowing(true)
        .asItemStack();
  }

  @Override
  public void onInteract(final @NotNull Player player, final @NotNull PlayerInteractEvent event) {
    event.setCancelled(true);
    if (event.getAction() != Action.RIGHT_CLICK_AIR
        && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    player.kickPlayer("A bientôt");
  }

  @Override
  public void onDrop(final @NotNull Player player, final @NotNull PlayerDropItemEvent event) {
    event.setCancelled(true);
  }

  @Override
  public void onInventoryClick(
      final @NotNull Player player, final @NotNull InventoryClickEvent event) {
    event.setCancelled(true);
  }
}
