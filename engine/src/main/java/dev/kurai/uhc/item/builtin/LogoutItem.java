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

public final class LogoutItem extends CustomItem {

  private static final String IDENTIFIER = "logout";

  public LogoutItem() {
    super(IDENTIFIER, false, false);
  }

  @Override
  public ItemStack provideIcon(final Player player) {
    return new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
        .name("&c&lQuitter&8 " + CC.SQUARE + " &7Clic-Droit")
        .lore("", "&7" + CC.BAR + "&f Permet de quitter la partie.", "")
        .glowing(true)
        .asItemStack();
  }

  @Override
  public void onInteract(final Player player, final PlayerInteractEvent event) {
    event.setCancelled(true);
    if (event.getAction() != Action.RIGHT_CLICK_AIR
        && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    player.kickPlayer("A bientôt");
  }

  @Override
  public void onDrop(final Player player, final PlayerDropItemEvent event) {
    event.setCancelled(true);
  }

  @Override
  public void onInventoryClick(final Player player, final InventoryClickEvent event) {
    event.setCancelled(true);
  }
}
