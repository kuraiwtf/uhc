package dev.kurai.uhc.item.builtin;

import static org.bukkit.event.block.Action.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.menu.ConfigurationMenu;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ConfigurationItem extends CustomItem {

  private static final String IDENTIFIER = "configuration";

  private final UltraHardcoreAPI ultraHardcore;

  public ConfigurationItem(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super(IDENTIFIER, true, false);
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public @NotNull ItemStack provideIcon(final @NotNull Player player) {
    return new ItemBuilder(Material.REDSTONE_COMPARATOR)
        .name("&c&lConfigurer la partie&8 " + CC.SQUARE + "&7 Clic-Droit")
        .lore("", "&7" + CC.BAR + "&f Permet de configurer la partie.", "")
        .glowing(true)
        .asItemStack();
  }

  @Override
  public void onInteract(final @NotNull Player player, final @NotNull PlayerInteractEvent event) {
    event.setCancelled(true);
    final var action = event.getAction();
    if (action != RIGHT_CLICK_AIR && action != RIGHT_CLICK_BLOCK) {
      return;
    }

    new ConfigurationMenu(player, this.ultraHardcore).open();
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
