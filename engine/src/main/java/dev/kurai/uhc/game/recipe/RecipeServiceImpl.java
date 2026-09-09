package dev.kurai.uhc.game.recipe;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jspecify.annotations.Nullable;

public final class RecipeServiceImpl implements RecipeService {

  private final Map<String, GameRecipe> recipes;
  private final Collection<GameRecipe> recipesView;

  public RecipeServiceImpl() {
    this.recipes = Maps.newHashMap();
    this.recipesView = Collections.unmodifiableCollection(this.recipes.values());

    final ShapedRecipe notchAppleRecipe =
        new ShapedRecipe(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1));
    notchAppleRecipe.shape("GGG", "GAG", "GGG");
    notchAppleRecipe.setIngredient('G', Material.GOLD_INGOT);
    notchAppleRecipe.setIngredient('A', Material.APPLE);
    this.insertRecipe("notch_apple", "Pomme de Notch", notchAppleRecipe);

    final ShapelessRecipe stringRecipe = new ShapelessRecipe(new ItemStack(Material.STRING));
    stringRecipe.addIngredient(4, Material.WOOL);
    this.insertRecipe("string", "Ficelle", stringRecipe);
  }

  @Override
  public Collection<GameRecipe> recipes() {
    return this.recipesView;
  }

  @Override
  public @Nullable GameRecipe recipe(final String key) {
    return this.recipes.get(key);
  }

  @Override
  public @Nullable GameRecipe recipe(final ItemStack result) {
    return this.recipesView.stream()
        .filter(gameRecipe -> gameRecipe.bukkitRecipe().getResult().isSimilar(result))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void insertRecipe(final String key, final String name, final Recipe recipe) {
    this.recipes.put(key, new GameRecipeImpl(key, name, recipe));

    Bukkit.addRecipe(recipe);
  }

  @Override
  public void removeRecipe(final String key) {
    this.recipes.remove(key);
  }
}
