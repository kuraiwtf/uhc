package dev.kurai.uhc.item.builtin;

import static org.bukkit.event.block.Action.*;

import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class JumpItem extends CustomItem {

  private static final String IDENTIFIER = "jump";

  public JumpItem() {
    super(IDENTIFIER, false, false);
  }

  @Override
  public ItemStack provideIcon(final Player player) {
    return new ItemBuilder(Material.FEATHER)
        .name("&3&lJump&8 " + CC.SQUARE + "&7 Clic-Droit")
        .lore("", "&7" + CC.BAR + "&f Permet de rejoindre le jump.", "")
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

    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
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
