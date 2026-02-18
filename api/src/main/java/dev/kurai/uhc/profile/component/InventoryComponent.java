package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import org.bukkit.inventory.ItemStack;

public final class InventoryComponent implements Component {

  private final ItemStack[] contents;
  private final ItemStack[] armor;

  public InventoryComponent(final ItemStack[] contents, final ItemStack[] armor) {
    this.contents = contents;
    this.armor = armor;
  }

  public ItemStack[] getContents() {
    return this.contents;
  }

  public ItemStack[] getArmor() {
    return this.armor;
  }
}
