package dev.kurai.uhc.game.configuration.border;

import static dev.kurai.uhc.util.api.option.Option.option;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.util.api.option.Option;

public final class BorderConfiguration {

  public static final Option<Integer> INITIAL_SIZE_OPTION = option(key("initial_size"), 1250);
  public static final Option<Integer> FINAL_SIZE_OPTION = option(key("final_size"), 300);
  public static final Option<Float> SHRINK_SPEED_OPTION = option(key("shrink_speed"), 1.0f);
  public static final Option<BorderType> BORDER_TYPE_OPTION =
      option(key("border_type"), BorderType.DAMAGE);
}
