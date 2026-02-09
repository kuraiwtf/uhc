package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import org.bukkit.inventory.ItemStack;

public final class InventoryEditorComponent implements Component {

  private final ItemStack[] savedInventory;
  private final ItemStack[] savedArmor;

  public InventoryEditorComponent(
      final ItemStack[] savedInventory, final ItemStack[] savedArmor) {
    this.savedInventory = savedInventory;
    this.savedArmor = savedArmor;
  }

  public ItemStack[] getSavedInventory() {
    return this.savedInventory;
  }

  public ItemStack[] getSavedArmor() {
    return this.savedArmor;
  }
}
