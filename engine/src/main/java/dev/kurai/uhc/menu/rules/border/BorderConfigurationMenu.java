package dev.kurai.uhc.menu.rules.border;

import static dev.kurai.uhc.game.configuration.border.BorderConfiguration.*;
import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.game.configuration.border.BorderType;
import dev.kurai.uhc.menu.rules.border.BorderSizeConfigurationMenu.BorderSizeType;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BorderConfigurationMenu extends Menu {

  public BorderConfigurationMenu(final @NotNull Player player) {
    super("Configuration de la bordure", MenuSize.THREE, player);
  }

  @Override
  public void setup(final BackgroundLayer back, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new ModernBorderTemplate(DyeColor.CYAN.getData(), DyeColor.GRAY.getData()));

    front.set(11, new InitialSizeButton());
    front.set(12, new FinalSizeButton());
    front.set(14, new ShrinkSpeedButton());
    front.set(15, new BorderTypeButton());
  }

  private static final class InitialSizeButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var size = INITIAL_SIZE_OPTION.getValue();
      return new ItemBuilder(Material.STAINED_GLASS)
          .data(DyeColor.LIGHT_BLUE.getData())
          .name("&a&lTaille initiale")
          .lore(
              "",
              "&a " + SQUARE + "&f Taille: &a" + size,
              "",
              "&7" + BAR + "&f La taille initiale de la",
              "  bordure au &adébut&f de la partie.",
              "")
          .amount(Math.min(size / 10, 64))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var sizeMenu =
          new BorderSizeConfigurationMenu(menu.getPlayer(), BorderSizeType.INITIAL);
      sizeMenu.setPreviousMenu(menu);
      sizeMenu.open();
    }
  }

  private static final class FinalSizeButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var size = FINAL_SIZE_OPTION.getValue();
      return new ItemBuilder(Material.STAINED_GLASS)
          .data(DyeColor.RED.getData())
          .name("&c&lTaille finale")
          .lore(
              "",
              "&c " + SQUARE + "&f Taille: &c" + size,
              "",
              "&7" + BAR + "&f La taille finale de la",
              "  bordure après &créduction&f.",
              "")
          .amount(Math.min(size / 10, 64))
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var sizeMenu = new BorderSizeConfigurationMenu(menu.getPlayer(), BorderSizeType.FINAL);
      sizeMenu.setPreviousMenu(menu);
      sizeMenu.open();
    }
  }

  private static final class ShrinkSpeedButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var speed = SHRINK_SPEED_OPTION.getValue();
      return new ItemBuilder(Material.FEATHER)
          .name("&b&lVitesse de réduction")
          .lore(
              "",
              "&b " + SQUARE + "&f Vitesse: &b" + String.format("%.1f", speed) + "x",
              "",
              "&7" + BAR + "&f La vitesse à laquelle",
              "  la bordure se &bréduit&f.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var menu = click.getMenu();
      final var speedMenu = new BorderSpeedConfigurationMenu(menu.getPlayer());
      speedMenu.setPreviousMenu(menu);
      speedMenu.open();
    }
  }

  private static final class BorderTypeButton extends Button {

    @Override
    public ItemStack getIcon() {
      final var type = BORDER_TYPE_OPTION.getValue();
      final var isDamage = type == BorderType.DAMAGE;

      return new ItemBuilder(isDamage ? Material.DIAMOND_SWORD : Material.ENDER_PEARL)
          .name("&d&lType de bordure")
          .lore(
              "",
              "&d " + SQUARE + "&f Type: " + (isDamage ? "&c" : "&b") + type.getName(),
              "",
              "&7" + BAR + "&f Le comportement de la",
              "  bordure pour les joueurs.",
              "",
              "&d " + SQUARE + "&f Dégâts:&f Inflige des dégâts",
              "&d " + SQUARE + "&f Téléportation:&f Téléporte en dehors",
              "",
              "&eCliquez pour changer",
              "")
          .addFlags(ItemFlag.HIDE_ATTRIBUTES)
          .glowing(!isDamage)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var currentType = BORDER_TYPE_OPTION.getValue();
      final var newType =
          currentType == BorderType.DAMAGE ? BorderType.TELEPORT : BorderType.DAMAGE;
      BORDER_TYPE_OPTION.setValue(newType);
      click.getMenu().update();
    }
  }
}
