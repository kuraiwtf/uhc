package dev.kurai.uhc.game.recipe;

import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jspecify.annotations.Nullable;

public interface RecipeService {

  Collection<GameRecipe> recipes();

  @Nullable GameRecipe recipe(final String key);

  @Nullable GameRecipe recipe(final ItemStack result);

  void insertRecipe(final String key, final String name, final Recipe recipe);

  void removeRecipe(final String key);
}
