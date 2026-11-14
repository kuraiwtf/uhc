package dev.kurai.uhc.menu.rules.cycle;

import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.game.cycle.CycleService;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import dev.kurai.uhc.util.TimeUtil;
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
public final class CycleTimeMenu extends Menu {

  private static final int MIN_DURATION = 5 * 60;
  private static final int MAX_DURATION = 30 * 60;

  private final CycleService cycleService;

  public CycleTimeMenu(final Player player, final CycleService cycleService) {
    super("Durée du cycle", MenuSize.THREE, player);
    this.cycleService = cycleService;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));

    front.set(10, new ValueModifierButton(DyeColor.RED, -5 * 60, this.cycleService));
    front.set(11, new ValueModifierButton(DyeColor.ORANGE, -60, this.cycleService));
    front.set(12, new ValueModifierButton(DyeColor.YELLOW, -30, this.cycleService));
    front.set(13, new ValueButton(this.cycleService));
    front.set(14, new ValueModifierButton(DyeColor.LIGHT_BLUE, 30, this.cycleService));
    front.set(15, new ValueModifierButton(DyeColor.LIME, 60, this.cycleService));
    front.set(16, new ValueModifierButton(DyeColor.GREEN, 5 * 60, this.cycleService));
  }

  private static final class ValueModifierButton extends Button {

    private final DyeColor color;
    private final int modifier;
    private final CycleService cycleService;

    private ValueModifierButton(
        final DyeColor color, final int modifier, final CycleService cycleService) {
      this.color = color;
      this.modifier = modifier;
      this.cycleService = cycleService;
    }

    @Override
    public ItemStack getIcon() {
      final String modifierStr = TimeUtil.formatDuration(Math.abs(this.modifier * 1000L));
      return new ItemBuilder(Material.BANNER)
          .data(this.color.getDyeData())
          .name((this.modifier > 0 ? "&a+" : "&c-") + modifierStr)
          .amount(Math.max(1, Math.abs(this.modifier / 60)))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.cycleService.totalCycleDuration(
          Math.clamp(
              this.cycleService.totalCycleDuration() + this.modifier,
              MIN_DURATION,
              MAX_DURATION));
      click.getMenu().update();
    }
  }

  private static final class ValueButton extends Button {

    private final CycleService cycleService;

    private ValueButton(final CycleService cycleService) {
      this.cycleService = cycleService;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.WATCH)
          .name("&6&lDurée du cycle")
          .lore(
              "",
              "&6 "
                  + SQUARE
                  + "&f Durée: &b"
                  + TimeUtil.formatDuration(this.cycleService.totalCycleDuration() * 1000L),
              "",
              "   &2" + SQUARE + "&f Min:&a " + TimeUtil.formatDuration(MIN_DURATION * 1000L),
              "   &4" + SQUARE + "&f Max:&c " + TimeUtil.formatDuration(MAX_DURATION * 1000L),
              "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
