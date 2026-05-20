package dev.kurai.uhc.menu.team;

import static dev.kurai.uhc.util.CC.*;

import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
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

public final class TeamConfigurationMenu extends Menu {

  private final TeamModule module;

  public TeamConfigurationMenu(final Player player, final TeamModule module) {
    super("Gestion des équipes", MenuSize.THREE, player);
    this.module = module;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer front) {
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new BorderTemplate(DyeColor.LIME.getData()));

    front.set(11, new TeamViewButton(this.module));
    front.set(12, new TeamSizeButton(this.module));
    front.set(14, new FriendlyFireButton(this.module));
    front.set(15, new RandomTeamButton(this.module));
  }

  @RequiredArgsConstructor
  private static final class TeamViewButton extends Button {

    private final TeamModule module;

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.EYE_OF_ENDER)
          .name("&a&lTeam View")
          .lore(
              "",
              "&a "
                  + SQUARE
                  + "&f État: &a&l%s".formatted(this.module.teamView() ? "&a&lOui" : "&c&lNon"),
              "",
              "&7" + BAR + "&f Cette fonctionnalité est réservée",
              "  aux joueurs utilisant",
              "  le &bLunar Client&f.",
              "")
          .amount(this.module.teamView() ? 1 : 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.module.teamView(!this.module.teamView());
      click.getMenu().update();
    }
  }

  @RequiredArgsConstructor
  private static final class TeamSizeButton extends Button {

    private final TeamModule module;

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.SADDLE)
          .name("&a&lTaille des équipes")
          .lore(
              "",
              "&a "
                  + SQUARE
                  + "&f Taille: &a"
                  + (this.module.ffa()
                      ? "FFA"
                      : "%dvs%d".formatted(this.module.teamSize(), this.module.teamSize())),
              "")
          .amount(this.module.teamSize())
          .asItemStack();
    }
  }

  @RequiredArgsConstructor
  private static final class FriendlyFireButton extends Button {

    private final TeamModule module;

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.DIAMOND_SWORD)
          .name("&a&lFriendly Fire")
          .lore(
              "",
              "&a "
                  + SQUARE
                  + "&f État: %s".formatted(this.module.friendlyFire() ? "&a&lOui" : "&c&lNon"),
              "")
          .addFlags(ItemFlag.HIDE_ATTRIBUTES)
          .amount(this.module.friendlyFire() ? 1 : 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.module.friendlyFire(!this.module.friendlyFire());
      click.getMenu().update();
    }
  }

  @RequiredArgsConstructor
  private static final class RandomTeamButton extends Button {

    private final TeamModule module;

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.NAME_TAG)
          .name("&a&lÉquipes aléatoires")
          .lore(
              "",
              "&a "
                  + SQUARE
                  + "&f État: %s".formatted(this.module.randomTeam() ? "&a&lOui" : "&c&lNon"),
              "")
          .amount(this.module.randomTeam() ? 1 : 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.module.randomTeam(!this.module.randomTeam());
      click.getMenu().update();
    }
  }
}
