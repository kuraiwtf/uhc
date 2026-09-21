package dev.kurai.uhc.item.builtin;

import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PlayerSpectatorItem extends CustomItem {

  public PlayerSpectatorItem() {
    super("player_spectator", 4, false, true);
  }

  @Override
  public ItemStack provideIcon(final Player player) {
    return new ItemBuilder(Material.COMPASS)
        .name("&b&lJoueurs&8 " + CC.SQUARE + "&7 Clic-Droit")
        .lore("", "&7" + CC.BAR + "&f Permet de voir les joueurs de la partie.", "")
        .glowing(true)
        .asItemStack();
  }
}
