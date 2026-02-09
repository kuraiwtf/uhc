package dev.kurai.uhc.menu.rules.ore;

import static dev.kurai.uhc.game.configuration.ore.OreConfiguration.*;

import dev.kurai.uhc.util.api.option.Option;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum OreType {
  IRON("&7&lFer", Material.IRON_ORE, "&7", IRON_LIMIT_OPTION),
  GOLD("&e&lOr", Material.GOLD_ORE, "&e", GOLD_LIMIT_OPTION),
  DIAMOND("&b&lDiamant", Material.DIAMOND_ORE, "&b", DIAMOND_LIMIT_OPTION);

  private final String name;
  private final Material material;
  private final String color;
  private final Option<Integer> option;

  OreType(
      final String name, final Material material, final String color, final Option<Integer> option) {
    this.name = name;
    this.material = material;
    this.color = color;
    this.option = option;
  }

  public String getName() {
    return this.name;
  }

  public Material getMaterial() {
    return this.material;
  }

  public String getColor() {
    return this.color;
  }

  public Option<Integer> getOption() {
    return this.option;
  }
}
