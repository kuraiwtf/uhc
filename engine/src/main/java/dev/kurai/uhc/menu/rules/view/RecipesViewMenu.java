package dev.kurai.uhc.menu.rules.view;

import static dev.kurai.uhc.util.CC.SQUARE;

import dev.kurai.uhc.game.recipe.GameRecipe;
import dev.kurai.uhc.game.recipe.RecipeService;
import dev.kurai.uhc.menu.button.ItemButton;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RecipesViewMenu extends PaginatedMenu {

  private final RecipeService recipeService;

  public RecipesViewMenu(final Player player, final RecipeService recipeService) {
    super("Recettes", MenuSize.FIVE, player);
    this.recipeService = recipeService;
  }

  @Override
  public void setup(final BackgroundLayer background, final ForegroundLayer foreground) {
    this.apply(new BorderTemplate(DyeColor.RED.getData()));
    this.apply(new BackTemplate(this.getPreviousMenu()));
    this.apply(new PaginationTemplate());

    foreground.center(new PaginationSlot(this));
  }

  @Override
  public List<Button> getEntries() {
    return this.recipeService.recipes().stream().map(RecipesViewMenu::button).toList();
  }

  private static Button button(final GameRecipe recipe) {
    return new ItemButton(
        new ItemBuilder(recipe.bukkitRecipe().getResult())
            .name("&6&l" + recipe.getName())
            .lore(
                "",
                "&6 "
                    + SQUARE
                    + "&r Statut: "
                    + (recipe.isEnabled() ? "&aFabriquable" : "&cBloquée"),
                "")
            .amount(recipe.isEnabled() ? 1 : 0)
            .asItemStack());
  }
}
