package dev.kurai.uhc.menu.button;

import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class GlassButton extends Button {

  private final int data;

  public GlassButton(final int data) {
    this.data = data;
  }

  @Contract(pure = true)
  @Override
  public @NotNull ItemStack getIcon() {
    return new ItemBuilder(Material.STAINED_GLASS_PANE)
        .data(this.data)
        .name("&0")
        .lunarTag("unclickable", true)
        .lunarTag("hideSlotHighlight", true)
        .lunarTag("hideItemTooltip", true)
        .asItemStack();
  }
}
