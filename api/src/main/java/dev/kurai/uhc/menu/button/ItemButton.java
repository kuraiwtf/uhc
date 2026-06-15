package dev.kurai.uhc.menu.button;

import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.button.Button;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ItemButton extends Button {

  private final ItemStack item;

  public ItemButton(final  ItemStack item) {
    this.item = item;
  }

  @Contract(pure = true)
  @Override
  public  ItemStack getIcon() {
    return new ItemBuilder(this.item).lunarTag("unclickable", true).asItemStack();
  }
}
