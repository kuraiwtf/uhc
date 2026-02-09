package dev.kurai.uhc.menu.rules.border;

import static dev.kurai.uhc.game.configuration.border.BorderConfiguration.*;
import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BorderSpeedConfigurationMenu extends Menu {

  public BorderSpeedConfigurationMenu(final Player player) {
    super("Vitesse de réduction", MenuSize.THREE, player);
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.CYAN.getData()));

    front.set(10, new ValueModifierButton(DyeColor.RED, -1.0f));
    front.set(11, new ValueModifierButton(DyeColor.ORANGE, -0.5f));
    front.set(12, new ValueModifierButton(DyeColor.YELLOW, -0.1f));
    front.set(13, new ValueButton());
    front.set(14, new ValueModifierButton(DyeColor.LIGHT_BLUE, 0.1f));
    front.set(15, new ValueModifierButton(DyeColor.LIME, 0.5f));
    front.set(16, new ValueModifierButton(DyeColor.GREEN, 1.0f));
  }

  private static final class ValueModifierButton extends Button {

    private final DyeColor color;
    private final float modifier;

    private ValueModifierButton(final DyeColor color, final float modifier) {
      this.color = color;
      this.modifier = modifier;
    }

    @Override
    public ItemStack getIcon() {
      final var modifierStr = String.format("%.1f", Math.abs(this.modifier));
      return new ItemBuilder(Material.BANNER)
          .data(this.color.getDyeData())
          .name(this.modifier > 0 ? "&a+" + modifierStr : "&c-" + modifierStr)
          .amount((int) Math.max(1, Math.abs(this.modifier * 10)))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var currentValue = SHRINK_SPEED_OPTION.getValue();
      final var newValue = Math.max(0.1f, Math.min(10.0f, currentValue + this.modifier));
      SHRINK_SPEED_OPTION.setValue(newValue);
      click.getMenu().update();
    }
  }

  private static final class ValueButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var speed = SHRINK_SPEED_OPTION.getValue();
      return new ItemBuilder(Material.FEATHER)
          .name("&b&lVitesse de réduction")
          .lore(
              "",
              "&b " + SQUARE + "&f Vitesse: &b" + String.format("%.1f", speed) + "x",
              "",
              "&7" + BAR + "&f La vitesse à laquelle",
              "  la bordure se &bréduit&f.",
              "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
