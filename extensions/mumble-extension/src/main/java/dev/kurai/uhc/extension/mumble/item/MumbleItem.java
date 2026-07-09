package dev.kurai.uhc.extension.mumble.item;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

import dev.kurai.uhc.extension.mumble.MumbleExtension;
import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class MumbleItem extends CustomItem {

  private final MumbleExtension extension;

  public MumbleItem(final MumbleExtension extension) {
    super("mumble", false, false);
    this.extension = extension;
  }

  @Override
  public ItemStack provideIcon(final Player player) {
    return new ItemBuilder(Material.IRON_INGOT)
        .name("&b&lMumble&8 " + CC.SQUARE + "&7 Clic-Droit")
        .glowing(true)
        .asItemStack();
  }

  @Override
  public void onInteract(final Player player, final PlayerInteractEvent event) {
    event.setCancelled(true);
    final var action = event.getAction();
    if (action != RIGHT_CLICK_AIR && action != RIGHT_CLICK_BLOCK) {
      return;
    }

    this.extension.advertise(player);
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
