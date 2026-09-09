package dev.kurai.uhc.game.recipe;

import org.bukkit.inventory.Recipe;

final class GameRecipeImpl implements GameRecipe {

  private final String key;
  private final String name;

  private final Recipe recipe;

  private boolean enabled;

  public GameRecipeImpl(final String key, final String name, final Recipe recipe) {
    this.key = key;
    this.name = name;

    this.recipe = recipe;

    this.enabled = true;
  }

  @Override
  public String getKey() {
    return this.key;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public Recipe bukkitRecipe() {
    return this.recipe;
  }
}
