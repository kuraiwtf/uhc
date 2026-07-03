package dev.kurai.uhc.menu.rules.game;

import static dev.kurai.uhc.game.configuration.game.GameConfiguration.BOW_HEALTH_VIEW_OPTION;
import static dev.kurai.uhc.game.configuration.game.GameConfiguration.OBSIDIAN_TRAP_OPTION;
import static dev.kurai.uhc.util.CC.SQUARE;

import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.ModernBorderTemplate;
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

public final class GameRulesMenu extends Menu {

  public GameRulesMenu(final Player player) {
    super("Règles du jeu", MenuSize.FIVE, player);
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new ModernBorderTemplate(DyeColor.RED.getData(), DyeColor.GRAY.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));

    foreground.set(11, new BowHealthViewButton());
    foreground.set(12, new ObsidianTrapButton());
  }

  private static final class BowHealthViewButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.BOW)
          .name("&c&lVie en touchant une flèche")
          .lore(
              "",
              "&c "
                  + SQUARE
                  + "&f Statut: "
                  + (BOW_HEALTH_VIEW_OPTION.getValue() ? "&a&lOui" : "&c&lNon"),
              "")
          .amount(BOW_HEALTH_VIEW_OPTION.getValue() ? 1 : 0)
          .glowing(BOW_HEALTH_VIEW_OPTION.getValue())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      BOW_HEALTH_VIEW_OPTION.setValue(!BOW_HEALTH_VIEW_OPTION.getValue());
      click.getMenu().update();
    }
  }

  private static final class ObsidianTrapButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.OBSIDIAN)
          .name("&d&lObsi Trap")
          .lore(
              "",
              "&d "
                  + SQUARE
                  + "&f Statut: "
                  + (OBSIDIAN_TRAP_OPTION.getValue() ? "&a&lOui" : "&c&lNon"),
              "")
          .amount(OBSIDIAN_TRAP_OPTION.getValue() ? 1 : 0)
          .glowing(OBSIDIAN_TRAP_OPTION.getValue())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      OBSIDIAN_TRAP_OPTION.setValue(!OBSIDIAN_TRAP_OPTION.getValue());
      click.getMenu().update();
    }
  }
}
