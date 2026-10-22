package dev.kurai.uhc.menu.rules.episode;

import static dev.kurai.uhc.game.configuration.border.BorderConfiguration.*;
import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.game.episode.EpisodeService;
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
public final class EpisodeTimeMenu extends Menu {

  private final EpisodeService episodeService;

  public EpisodeTimeMenu(final Player player, final EpisodeService episodeService) {
    super("Durée d'épisode", MenuSize.THREE, player);
    this.episodeService = episodeService;
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.CYAN.getData()));

    front.set(10, new ValueModifierButton(DyeColor.RED, -5 * 60, this.episodeService));
    front.set(11, new ValueModifierButton(DyeColor.ORANGE, -60, this.episodeService));
    front.set(12, new ValueModifierButton(DyeColor.YELLOW, -30, this.episodeService));
    front.set(13, new ValueButton(this.episodeService));
    front.set(14, new ValueModifierButton(DyeColor.LIGHT_BLUE, 30, this.episodeService));
    front.set(15, new ValueModifierButton(DyeColor.LIME, 60, this.episodeService));
    front.set(16, new ValueModifierButton(DyeColor.GREEN, 5 * 60, this.episodeService));
  }

  private static final class ValueModifierButton extends Button {

    private final DyeColor color;
    private final int modifier;
    private final EpisodeService episodeService;

    private ValueModifierButton(
        final DyeColor color, final int modifier, final EpisodeService episodeService) {
      this.color = color;
      this.modifier = modifier;
      this.episodeService = episodeService;
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
      this.episodeService.setTimePerEpisode(
          Math.clamp(this.episodeService.getTimePerEpisode() + this.modifier, 5 * 60, 20 * 60));
      click.getMenu().update();
    }
  }

  private static final class ValueButton extends Button {

    private final EpisodeService episodeService;

    private ValueButton(final EpisodeService episodeService) {
      this.episodeService = episodeService;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BOOKSHELF)
          .name("&b&lDurée d'épisode")
          .lore(
              "",
              "&b "
                  + SQUARE
                  + "&f Durée: &b"
                  + TimeUtil.formatDuration(this.episodeService.getTimePerEpisode() * 1000L),
              "")
          .lunarTag("unclickable", true)
          .lunarTag("hideSlotHighlight", true)
          .asItemStack();
    }
  }
}
