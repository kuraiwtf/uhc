package dev.kurai.uhc.menu.rules.drop;

import static org.bukkit.DyeColor.*;

import dev.kurai.uhc.game.drop.AbstractDropRateModifier;
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
public final class DropRateConfigurationMenu extends Menu {

  private final AbstractDropRateModifier modifier;

  public DropRateConfigurationMenu(final Player player, final AbstractDropRateModifier modifier) {
    super(modifier.getName(), MenuSize.THREE, player);
    this.modifier = modifier;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(LIME.getData()));

    front.set(10, new ValueModifierButton(this.modifier, RED, -10));
    front.set(11, new ValueModifierButton(this.modifier, ORANGE, -5));
    front.set(12, new ValueModifierButton(this.modifier, YELLOW, -1));
    front.set(13, new ValueButton(this.modifier));
    front.set(14, new ValueModifierButton(this.modifier, LIGHT_BLUE, 1));
    front.set(15, new ValueModifierButton(this.modifier, LIME, 5));
    front.set(16, new ValueModifierButton(this.modifier, GREEN, 10));
  }

  private static final class ValueModifierButton extends Button {

    private final AbstractDropRateModifier dropRateModifier;
    private final DyeColor color;
    private final int modifier;

    private ValueModifierButton(
        final AbstractDropRateModifier dropRateModifier, final DyeColor color, final int modifier) {
      this.dropRateModifier = dropRateModifier;
      this.color = color;
      this.modifier = modifier;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BANNER)
          .data(this.color.getDyeData())
          .name(this.modifier > 0 ? "&a+" + this.modifier : "&c" + this.modifier)
          .amount(this.modifier)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.dropRateModifier.setDropRate(
          Math.max(Math.min(100, this.dropRateModifier.getDropRate() + this.modifier), 0));
      click.getMenu().update();
    }
  }

  private static final class ValueButton extends Button {

    private final AbstractDropRateModifier modifier;

    private ValueButton(final AbstractDropRateModifier modifier) {
      this.modifier = modifier;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.modifier.getIcon())
          .amount(this.modifier.getDropRate())
          .name("&a&l" + this.modifier.getName())
          .lore(
              "",
              "&a " + CC.SQUARE + "&f Taux de drop: &a%d%%".formatted(this.modifier.getDropRate()),
              "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
