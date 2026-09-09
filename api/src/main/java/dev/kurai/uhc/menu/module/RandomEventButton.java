package dev.kurai.uhc.menu.module;

import dev.kurai.uhc.module.event.ModuleEventHolder;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class RandomEventButton extends Button {

  private final ModuleEventHolder eventHolder;

  public RandomEventButton(final ModuleEventHolder eventHolder) {
    this.eventHolder = eventHolder;
  }

  @Override
  public ItemStack getIcon() {
    return new ItemBuilder(Material.ENCHANTED_BOOK)
        .name("§a§lÉvènements Aléatoires")
        .lore("", "§fPermet de modifier les", "§févènements aléatoires de", "§fla partie.", "")
        .asItemStack();
  }
}
