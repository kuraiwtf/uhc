package dev.kurai.uhc.menu.slots;

import static dev.kurai.uhc.game.GameService.SLOTS_OPTION;
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
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class SlotsConfigurationMenu extends Menu {

  public SlotsConfigurationMenu(final Player player) {
    super("Slots", MenuSize.THREE, player);
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(LIME.getData()));

    front.set(10, new ValueModifierButton(RED, -5));
    front.set(11, new ValueModifierButton(ORANGE, -3));
    front.set(12, new ValueModifierButton(YELLOW, -1));
    front.set(13, new ValueButton());
    front.set(14, new ValueModifierButton(LIGHT_BLUE, 1));
    front.set(15, new ValueModifierButton(LIME, 3));
    front.set(16, new ValueModifierButton(GREEN, 5));
  }

  private static final class ValueModifierButton extends Button {

    private final DyeColor color;
    private final int modifier;

    private ValueModifierButton(final @NotNull DyeColor color, final int modifier) {
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
      SLOTS_OPTION.setValue(
          Math.max(SLOTS_OPTION.getValue() + this.modifier, Bukkit.getOnlinePlayers().size()));
      click.getMenu().update();
    }
  }

  private static final class ValueButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.SKULL_ITEM)
          .data(3)
          .amount(SLOTS_OPTION.getValue())
          .name("&a&lSlots")
          .lore("", "&7" + CC.BAR + "&f Slots: &a" + SLOTS_OPTION.getValue(), "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
