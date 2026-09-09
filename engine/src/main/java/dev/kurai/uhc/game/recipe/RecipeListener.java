package dev.kurai.uhc.game.recipe;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public final class RecipeListener implements Listener {

  private final RecipeService recipeService;

  public RecipeListener(final RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @EventHandler
  public void onPrepareCraft(final PrepareItemCraftEvent event) {
    final var recipe = event.getRecipe();
    if (recipe == null) {
      return;
    }

    final GameRecipe gameRecipe = this.recipeService.recipe(recipe.getResult());
    if (gameRecipe != null && !gameRecipe.isEnabled()) {
      event.getInventory().setResult(null);
    }
  }
}
