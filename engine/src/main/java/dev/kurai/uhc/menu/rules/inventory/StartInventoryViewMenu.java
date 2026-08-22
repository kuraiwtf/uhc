package dev.kurai.uhc.menu.rules.inventory;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.menu.button.GlassButton;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class StartInventoryViewMenu extends Menu {

  private static final Button GLASS = new GlassButton(DyeColor.GRAY.getData());

  private final UltraHardcoreAPI ultraHardcore;

  public StartInventoryViewMenu(final Player player, final UltraHardcoreAPI ultraHardcore) {
    super("Inventaire de départ", MenuSize.SIX, player);
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));

    final var savedInventory = InventoryConfiguration.INVENTORY_CONTENT_OPTION.getValue();
    final var savedArmor = InventoryConfiguration.INVENTORY_ARMOR_OPTION.getValue();

    back.fill(36, 53, GLASS);

    this.setupInventory(front, savedInventory);
    this.setupArmor(front, savedArmor);
  }

  private void setupInventory(final ForegroundLayer front, final ItemStack[] savedInventory) {
    for (int i = 0; i < savedInventory.length; i++) {
      final var item = savedInventory[i];
      if (item != null && item.getType() != Material.AIR) {
        front.set(i, new ItemButton(item));
      }
    }
  }

  private void setupArmor(final ForegroundLayer front, final ItemStack[] savedArmor) {
    final var armorSlots =
        new ArmorSlot[] {
          new ArmorSlot(45, 0, Material.CHAINMAIL_BOOTS, "Bottes"),
          new ArmorSlot(46, 1, Material.CHAINMAIL_LEGGINGS, "Pantalon"),
          new ArmorSlot(47, 2, Material.CHAINMAIL_CHESTPLATE, "Plastron"),
          new ArmorSlot(48, 3, Material.CHAINMAIL_HELMET, "Casque")
        };

    for (final var armorSlot : armorSlots) {
      final var item = savedArmor[armorSlot.armorIndex];
      if (item != null && item.getType() != Material.AIR) {
        front.set(armorSlot.menuSlot, new ItemButton(item));
      } else {
        front.set(
            armorSlot.menuSlot,
            new ArmorPlaceholderButton(armorSlot.placeholderMaterial, armorSlot.name));
      }
    }
  }

  private record ArmorSlot(
      int menuSlot, int armorIndex, Material placeholderMaterial, String name) {}

  private static final class ArmorPlaceholderButton extends Button {

    private final Material armorType;
    private final String armorName;

    private ArmorPlaceholderButton(final Material armorType, final String armorName) {
      this.armorType = armorType;
      this.armorName = armorName;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.armorType)
          .name("&c&l" + this.armorName)
          .lore("", "&7Aucune pièce d'armure", "&7configurée pour ce slot.", "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
