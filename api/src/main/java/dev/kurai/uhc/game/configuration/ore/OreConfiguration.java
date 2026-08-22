package dev.kurai.uhc.game.configuration.ore;

import static dev.kurai.uhc.util.api.option.Option.positiveNumberOption;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.util.api.option.Option;

public final class OreConfiguration {

  public static final Option<Integer> IRON_LIMIT_OPTION =
      positiveNumberOption(key("iron_limit"), 0);
  public static final Option<Integer> GOLD_LIMIT_OPTION =
      positiveNumberOption(key("gold_limit"), 0);
  public static final Option<Integer> DIAMOND_LIMIT_OPTION =
      positiveNumberOption(key("diamond_limit"), 0);

  private OreConfiguration() {}
}
