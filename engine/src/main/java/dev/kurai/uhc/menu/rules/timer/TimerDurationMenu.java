package dev.kurai.uhc.menu.rules.timer;

import static dev.kurai.uhc.util.CC.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.timer.AbstractTimer;
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
public final class TimerDurationMenu extends Menu {

  private final AbstractTimer timer;

  public TimerDurationMenu(final Player player, final AbstractTimer timer) {
    super("Configuration: " + timer.getName(), MenuSize.THREE, player);
    this.timer = timer;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));

    front.set(10, new TimeModifierButton(this.timer, DyeColor.RED, -5 * 60));
    front.set(11, new TimeModifierButton(this.timer, DyeColor.ORANGE, -60));
    front.set(12, new TimeModifierButton(this.timer, DyeColor.YELLOW, -30));
    front.set(13, new CurrentValueButton(this.timer));
    front.set(14, new TimeModifierButton(this.timer, DyeColor.LIGHT_BLUE, 30));
    front.set(15, new TimeModifierButton(this.timer, DyeColor.LIME, 60));
    front.set(16, new TimeModifierButton(this.timer, DyeColor.GREEN, 5 * 60));
  }

  private static final class TimeModifierButton extends Button {

    private final AbstractTimer timer;
    private final DyeColor color;
    private final int modifier;

    private TimeModifierButton(
        final AbstractTimer timer, final DyeColor color, final int modifier) {
      this.timer = timer;
      this.color = color;
      this.modifier = modifier;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BANNER)
          .data(this.color.getDyeData())
          .name((this.modifier > 0 ? "&a+" : "&c") + TimeUtil.formatDuration(this.modifier * 1000L))
          .amount(Math.min(Math.abs(this.modifier) / 60, 64))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var currentTime = this.timer.getTimeLeft();
      final var minTime = this.timer.getMinDuration();
      final var maxTime = this.timer.getMaxDuration();

      var newTime = currentTime + this.modifier;

      newTime = Math.max(minTime, newTime);
      if (maxTime != -1) {
        newTime = Math.min(maxTime, newTime);
      }

      this.timer.setTimeLeft(newTime);
      click.getMenu().update();
    }
  }

  private static final class CurrentValueButton extends Button {

    private final AbstractTimer timer;

    private CurrentValueButton(final AbstractTimer timer) {
      this.timer = timer;
    }

    @Override
    public ItemStack getIcon() {
      final var timeLeft = this.timer.getTimeLeft();
      final var minTime = this.timer.getMinDuration();
      final var maxTime = this.timer.getMaxDuration();

      final var lines = Lists.<String>newArrayList();
      lines.add("");
      lines.add(
          "&6 " + SQUARE + "&f Durée actuelle: &b" + TimeUtil.formatDuration(timeLeft * 1000L));

      if (minTime > 0 || maxTime != -1) {
        lines.add("&6 " + SQUARE + "&f Limites:");
        if (minTime > 0) {
          lines.add("   &2" + SQUARE + "&f Min:&a " + TimeUtil.formatDuration(minTime * 1000L));
        }

        if (maxTime != -1) {
          lines.add("   &4" + SQUARE + "&f Max:&c " + TimeUtil.formatDuration(maxTime * 1000L));
        }
      }

      lines.add("");
      lines.add("&7" + BAR + "&f Modifiez le temps avec");
      lines.add("  les boutons autour.");
      lines.add("");

      return new ItemBuilder(Material.WATCH)
          .name("&6&l" + this.timer.getName())
          .lore(lines)
          .amount(Math.clamp(timeLeft / 60, 1, 64))
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
