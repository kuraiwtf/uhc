package dev.kurai.uhc.menu.template;

import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.template.Template;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class BackTemplate implements Template {

  private final Menu parentMenu;

  public BackTemplate(final Menu parentMenu) {
    this.parentMenu = parentMenu;
  }

  @Override
  public void apply(final BackgroundLayer background, final ForegroundLayer foreground) {
    if (this.parentMenu == null) {
      return;
    }

    foreground.set(
        background.getMenu().getTotalSlots() - 5,
        new Button() {

          @Override
          public ItemStack getIcon() {
            return new ItemBuilder(Material.SKULL_ITEM)
                .data(3)
                .name("&cRevenir en arrière")
                .url(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNhOTQyYTkyOWQzNjc5YTI2ZjAwOTIwMDVlOTg1YTU1ZTc4OGExYjU2NDkxMDEzMDc0OWQ5ZmM4OTZlOTAxMyJ9fX0")
                .asItemStack();
          }

          @Override
          public void onClick(final ButtonClick click) {
            BackTemplate.this.parentMenu.open();
          }
        });
  }
}
