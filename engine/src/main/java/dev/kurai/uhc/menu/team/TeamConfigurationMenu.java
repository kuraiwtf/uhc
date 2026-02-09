package dev.kurai.uhc.menu.team;

import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public final class TeamConfigurationMenu extends Menu {

  public TeamConfigurationMenu(final Player player) {
    super("Gestion des équipes", MenuSize.THREE, player);
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.LIME.getData()));

    front.set(11, new TeamViewButton());
    front.set(12, new TeamSizeButton());
    front.set(14, new FriendlyFireButton());
    front.set(15, new RandomTeamButton());
  }

  private static final class TeamViewButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.EYE_OF_ENDER)
          .name("&a&lTeam View")
          .lore(
              "",
              "&a " + SQUARE + "&f État: &a&lOui",
              "",
              "&7" + BAR + "&f Cette fonctionnalité est réservée",
              "  aux joueurs utilisant",
              "  le &bLunar Client&f.",
              "")
          .asItemStack();
    }
  }

  private static final class TeamSizeButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.SADDLE)
          .name("&a&lTaille des équipes")
          .lore("", "&a " + SQUARE + "&f Taille: &a" + (1 == 1 ? "FFA" : 1 + "vs" + 1), "")
          .asItemStack();
    }
  }

  private static final class FriendlyFireButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.DIAMOND_SWORD)
          .name("&a&lFriendly Fire")
          .lore("", "&a " + SQUARE + "&f État: &a&lOui", "")
          .addFlags(ItemFlag.HIDE_ATTRIBUTES)
          .asItemStack();
    }
  }

  private static final class RandomTeamButton extends Button {

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.NAME_TAG)
          .name("&a&lÉquipes aléatoires")
          .lore("", "&a " + SQUARE + "&f État: &c&lNon", "")
          .asItemStack();
    }
  }
}
