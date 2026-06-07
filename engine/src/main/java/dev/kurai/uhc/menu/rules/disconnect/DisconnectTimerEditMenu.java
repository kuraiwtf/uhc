package dev.kurai.uhc.menu.rules.disconnect;

import static dev.kurai.uhc.util.CC.BAR;
import static dev.kurai.uhc.util.CC.SQUARE;

import com.google.common.collect.Lists;
import dev.kurai.uhc.game.disconnect.DisconnectService;
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

public final class DisconnectTimerEditMenu extends Menu {

  private final DisconnectService disconnectService;

  public DisconnectTimerEditMenu(final Player player, final DisconnectService disconnectService) {
    super("Déconnexion", MenuSize.THREE, player);
    this.disconnectService = disconnectService;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.RED.getData()));

    front.set(10, new TimeModifierButton(this.disconnectService, DyeColor.RED, -5 * 60));
    front.set(11, new TimeModifierButton(this.disconnectService, DyeColor.ORANGE, -60));
    front.set(12, new TimeModifierButton(this.disconnectService, DyeColor.YELLOW, -30));
    front.set(13, new CurrentValueButton(this.disconnectService));
    front.set(14, new TimeModifierButton(this.disconnectService, DyeColor.LIGHT_BLUE, 30));
    front.set(15, new TimeModifierButton(this.disconnectService, DyeColor.LIME, 60));
    front.set(16, new TimeModifierButton(this.disconnectService, DyeColor.GREEN, 5 * 60));
  }

  private static final class TimeModifierButton extends Button {

    private final DisconnectService disconnectService;
    private final DyeColor color;
    private final int modifier;

    private TimeModifierButton(
        final DisconnectService disconnectService, final DyeColor color, final int modifier) {
      this.disconnectService = disconnectService;
      this.color = color;
      this.modifier = modifier;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BANNER)
          .data(this.color.getDyeData())
          .name((this.modifier > 0 ? "&a+" : "&c") + TimeUtil.formatDuration(this.modifier * 1000L))
          .amount(this.modifier / 60)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.disconnectService.disconnectTime(
          Math.clamp(
              this.disconnectService.disconnectTime() + (this.modifier * 1_000L),
              5 * 60 * 1_000L,
              20 * 60 * 1_000L));
      click.getMenu().update();
    }
  }

  private static final class CurrentValueButton extends Button {

    private final DisconnectService disconnectService;

    private CurrentValueButton(final DisconnectService service) {
      this.disconnectService = service;
    }

    @Override
    public ItemStack getIcon() {
      final var lines = Lists.<String>newArrayList();
      lines.add("");
      lines.add(
          "&6 "
              + SQUARE
              + "&f Durée actuelle: &b"
              + TimeUtil.formatDuration(this.disconnectService.disconnectTime()));
      lines.add("");
      lines.add("&7" + BAR + "&f Modifiez le temps avec");
      lines.add("  les boutons autour.");
      lines.add("");

      return new ItemBuilder(Material.WATCH)
          .name("&6&lTemps de déconnexion")
          .lore(lines)
          .amount(Math.clamp(this.disconnectService.disconnectTime() / (60 * 1_000L), 1, 64))
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
