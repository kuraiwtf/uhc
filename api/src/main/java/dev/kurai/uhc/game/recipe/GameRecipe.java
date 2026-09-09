package dev.kurai.uhc.game.recipe;

import dev.kurai.uhc.util.api.key.Keyed;
import dev.kurai.uhc.util.api.name.Nameable;
import org.bukkit.inventory.Recipe;

public interface GameRecipe extends Keyed<String>, Nameable<String> {

  @Override
  String getKey();

  @Override
  String getName();

  boolean isEnabled();

  void setEnabled(final boolean enabled);

  Recipe bukkitRecipe();
}
