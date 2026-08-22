package dev.kurai.uhc.menu.slots;

import static org.bukkit.DyeColor.*;

import dev.kurai.uhc.game.slot.SlotProvider;
import dev.kurai.uhc.game.slot.impl.MutableSlotProvider;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
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

public final class SlotsConfigurationMenu extends Menu {

  private final MutableSlotProvider slotProvider;

  public SlotsConfigurationMenu(final Player player, final MutableSlotProvider slotProvider) {
    super("Slots", MenuSize.THREE, player);
    this.slotProvider = slotProvider;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(LIME.getData()));

    front.set(10, new ValueModifierButton(this.slotProvider, RED, -5));
    front.set(11, new ValueModifierButton(this.slotProvider, ORANGE, -3));
    front.set(12, new ValueModifierButton(this.slotProvider, YELLOW, -1));
    front.set(13, new ValueButton(this.slotProvider));
    front.set(14, new ValueModifierButton(this.slotProvider, LIGHT_BLUE, 1));
    front.set(15, new ValueModifierButton(this.slotProvider, LIME, 3));
    front.set(16, new ValueModifierButton(this.slotProvider, GREEN, 5));
  }

  @RequiredArgsConstructor
  private static final class ValueModifierButton extends Button {

    private final MutableSlotProvider slotProvider;
    private final DyeColor color;
    private final int modifier;

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
      this.slotProvider.slots(
          Math.clamp(
              this.slotProvider.slots() + this.modifier,
              Bukkit.getOnlinePlayers().size(),
              Integer.MAX_VALUE));
      click.getMenu().update();
    }
  }

  @RequiredArgsConstructor
  private static final class ValueButton extends Button {

    private final SlotProvider slotProvider;

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.SKULL_ITEM)
          .data(3)
          .amount(this.slotProvider.slots())
          .name("&a&lSlots")
          .lore("", "&7" + CC.BAR + "&f Slots: &a" + this.slotProvider.slots(), "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
