package dev.kurai.uhc.menu.rules.recipe;

import dev.kurai.uhc.game.recipe.GameRecipe;
import dev.kurai.uhc.game.recipe.RecipeService;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class RecipeMenu extends PaginatedMenu {

  private final RecipeService recipeService;

  public RecipeMenu(final Player player, final RecipeService recipeService) {
    super("Recettes", MenuSize.FIVE, player);
    this.recipeService = recipeService;
  }

  @Override
  public List<Button> getEntries() {
    return this.recipeService.recipes().stream()
        .map(RecipeButton::new)
        .map(Button.class::cast)
        .toList();
  }

  @Override
  public void setup(final BackgroundLayer ignored, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.RED.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));

    foreground.center(new PaginationSlot(this));
  }

  private static final class RecipeButton extends Button {

    private final GameRecipe recipe;

    private RecipeButton(final GameRecipe recipe) {
      this.recipe = recipe;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(this.recipe.bukkitRecipe().getResult())
          .name("&6&l" + this.recipe.getName())
          .lore(
              "",
              "&6 "
                  + CC.SQUARE
                  + "&r Statut: "
                  + (this.recipe.isEnabled() ? "&aFabriquable" : "&cBloquée"),
              "")
          .amount(this.recipe.isEnabled() ? 1 : 0)
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.recipe.setEnabled(!this.recipe.isEnabled());
      click.getMenu().update();
    }
  }
}
