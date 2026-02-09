package dev.kurai.uhc.menu.rules.ore;

import static org.bukkit.DyeColor.*;

import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.CC;
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
public final class OreLimitConfigurationMenu extends Menu {

  private final OreType oreType;

  public OreLimitConfigurationMenu(final Player player, final OreType oreType) {
    super("Limite de " + oreType.getName().replaceAll("&[0-9a-fl]", ""), MenuSize.THREE, player);
    this.oreType = oreType;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(CYAN.getData()));

    front.set(10, new ValueModifierButton(this.oreType, RED, -10));
    front.set(11, new ValueModifierButton(this.oreType, ORANGE, -5));
    front.set(12, new ValueModifierButton(this.oreType, YELLOW, -1));
    front.set(13, new ValueButton(this.oreType));
    front.set(14, new ValueModifierButton(this.oreType, LIGHT_BLUE, 1));
    front.set(15, new ValueModifierButton(this.oreType, LIME, 5));
    front.set(16, new ValueModifierButton(this.oreType, GREEN, 10));
  }

  private static final class ValueModifierButton extends Button {

    private final OreType oreType;
    private final DyeColor color;
    private final int modifier;

    private ValueModifierButton(final OreType oreType, final DyeColor color, final int modifier) {
      this.oreType = oreType;
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
      final var option = this.oreType.getOption();
      final var currentValue = option.getValue();
      final var newValue = Math.max(0, currentValue + this.modifier);
      option.setValue(newValue);
      click.getMenu().update();
    }
  }

  private static final class ValueButton extends Button {

    private final OreType oreType;

    private ValueButton(final OreType oreType) {
      this.oreType = oreType;
    }

    @Override
    public ItemStack getIcon() {
      final int limit = this.oreType.getOption().getValue();
      return new ItemBuilder(this.oreType.getMaterial())
          .amount(limit)
          .name(this.oreType.getName())
          .lore(
              "",
              this.oreType.getColor()
                  + " "
                  + CC.SQUARE
                  + "&f Limite: "
                  + (limit == 0 ? "&cAucune" : this.oreType.getColor() + limit),
              "",
              "&7" + CC.BAR + "&f Une limite de &a0&f signifie",
              "  qu'il n'y a &aaucune limite&f.",
              "")
          .glowing(limit > 0)
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
