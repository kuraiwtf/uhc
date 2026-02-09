package dev.kurai.uhc.menu.rules.ore;

import static dev.kurai.uhc.game.configuration.ore.OreConfiguration.*;
import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.menu.button.ItemButton;
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
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class OreLimitMenu extends Menu {

  public OreLimitMenu(final Player player) {
    super("Limites des minerais", MenuSize.THREE, player);
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new ModernBorderTemplate(DyeColor.CYAN.getData(), DyeColor.GRAY.getData()));

    front.set(
        4,
        new ItemButton(
            new ItemBuilder(Material.PAPER)
                .name("&e&lInformations")
                .lore(
                    "",
                    "&7" + BAR + "&f Les limites de minerais",
                    "  permettent de définir le",
                    "  nombre maximum de chaque",
                    "  minerai qu'un joueur peut",
                    "  &cminer&f pendant la partie.",
                    "",
                    "&7" + BAR + "&f Une limite de &a0&f signifie",
                    "  qu'il n'y a &aaucune limite&f.",
                    "")
                .asItemStack()));

    front.set(11, new IronLimitButton());
    front.set(13, new GoldLimitButton());
    front.set(15, new DiamondLimitButton());
  }

  private static final class IronLimitButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var limit = IRON_LIMIT_OPTION.getValue();
      return new ItemBuilder(Material.IRON_ORE)
          .name("&7&lFer")
          .lore(
              "",
              "&7 " + SQUARE + "&f Limite: " + (limit == 0 ? "&cAucune" : "&7" + limit),
              "",
              "&7" + BAR + "&f Cliquez pour modifier",
              "  la limite de &7fer&f.",
              "")
          .amount(limit)
          .glowing(limit > 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var configMenu = new OreLimitConfigurationMenu(menu.getPlayer(), OreType.IRON);
      configMenu.setPreviousMenu(menu);
      configMenu.open();
    }
  }

  private static final class GoldLimitButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var limit = GOLD_LIMIT_OPTION.getValue();
      return new ItemBuilder(Material.GOLD_ORE)
          .name("&e&lOr")
          .lore(
              "",
              "&e " + SQUARE + "&f Limite: " + (limit == 0 ? "&cAucune" : "&e" + limit),
              "",
              "&7" + BAR + "&f Cliquez pour modifier",
              "  la limite d'&eor&f.",
              "")
          .amount(limit)
          .glowing(limit > 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var configMenu = new OreLimitConfigurationMenu(menu.getPlayer(), OreType.GOLD);
      configMenu.setPreviousMenu(menu);
      configMenu.open();
    }
  }

  private static final class DiamondLimitButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var limit = DIAMOND_LIMIT_OPTION.getValue();
      return new ItemBuilder(Material.DIAMOND_ORE)
          .name("&b&lDiamant")
          .lore(
              "",
              "&b " + SQUARE + "&f Limite: " + (limit == 0 ? "&cAucune" : "&b" + limit),
              "",
              "&7" + BAR + "&f Cliquez pour modifier",
              "  la limite de &bdiamant&f.",
              "")
          .amount(limit)
          .glowing(limit > 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var configMenu = new OreLimitConfigurationMenu(menu.getPlayer(), OreType.DIAMOND);
      configMenu.setPreviousMenu(menu);
      configMenu.open();
    }
  }
}
