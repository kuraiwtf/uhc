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
public final class BorderSizeConfigurationMenu extends Menu {

  private final BorderSizeType type;

  public BorderSizeConfigurationMenu(final Player player, final BorderSizeType type) {
    super(
        type == BorderSizeType.INITIAL ? "Taille initiale" : "Taille finale",
        MenuSize.THREE,
        player);
    this.type = type;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(
        new BorderTemplate(
            this.type == BorderSizeType.INITIAL
                ? DyeColor.GREEN.getData()
                : DyeColor.RED.getData()));

    front.set(10, new ValueModifierButton(this.type, DyeColor.RED, -100));
    front.set(11, new ValueModifierButton(this.type, DyeColor.ORANGE, -50));
    front.set(12, new ValueModifierButton(this.type, DyeColor.YELLOW, -10));
    front.set(13, new ValueButton(this.type));
    front.set(14, new ValueModifierButton(this.type, DyeColor.LIGHT_BLUE, 10));
    front.set(15, new ValueModifierButton(this.type, DyeColor.LIME, 50));
    front.set(16, new ValueModifierButton(this.type, DyeColor.GREEN, 100));
  }

  private static final class ValueModifierButton extends Button {

    private final BorderSizeType type;
    private final DyeColor color;
    private final int modifier;

    private ValueModifierButton(
        final BorderSizeType type, final DyeColor color, final int modifier) {
      this.type = type;
      this.color = color;
      this.modifier = modifier;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BANNER)
          .data(this.color.getDyeData())
          .name(this.modifier > 0 ? "&a+" + this.modifier : "&c" + this.modifier)
          .amount(Math.abs(this.modifier))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var option =
          this.type == BorderSizeType.INITIAL ? INITIAL_SIZE_OPTION : FINAL_SIZE_OPTION;
      final var currentValue = option.getValue();
      final int min = this.type == BorderSizeType.INITIAL ? 100 : 50;
      final int max = this.type == BorderSizeType.INITIAL ? 5000 : 2500;
      final var newValue = Math.max(min, Math.min(max, currentValue + this.modifier));
      option.setValue(newValue);
      click.getMenu().update();
    }
  }

  private static final class ValueButton extends Button {

    private final BorderSizeType type;

    private ValueButton(final BorderSizeType type) {
      this.type = type;
    }

    @Override
    public ItemStack getIcon() {
      final var option =
          this.type == BorderSizeType.INITIAL ? INITIAL_SIZE_OPTION : FINAL_SIZE_OPTION;
      final var size = option.getValue();
      final var color = this.type == BorderSizeType.INITIAL ? "&a" : "&c";
      final var dyeColor =
          this.type == BorderSizeType.INITIAL ? DyeColor.GREEN : DyeColor.RED;

      return new ItemBuilder(Material.STAINED_GLASS)
          .data(dyeColor.getData())
          .name(color + "&l" + (this.type == BorderSizeType.INITIAL ? "Taille initiale" : "Taille finale"))
          .lore(
              "",
              color + " " + SQUARE + "&f Taille: " + color + size,
              "",
              "&7"
                  + BAR
                  + "&f La taille "
                  + (this.type == BorderSizeType.INITIAL ? "initiale" : "finale")
                  + " de la",
              "  bordure"
                  + (this.type == BorderSizeType.INITIAL ? " au &adébut&f de la partie." : " après &créduction&f."),
              "")
          .amount(Math.min(size / 10, 64))
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }

  public enum BorderSizeType {
    INITIAL,
    FINAL
  }
}
