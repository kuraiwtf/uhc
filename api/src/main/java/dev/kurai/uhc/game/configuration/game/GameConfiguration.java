package dev.kurai.uhc.game.configuration.game;

import static dev.kurai.uhc.util.api.option.Option.option;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.util.api.option.Option;

public final class GameConfiguration {

  public static final Option<Boolean> BOW_HEALTH_VIEW_OPTION =
      option(key("bow_health_view"), false);

  public static final Option<Boolean> SPECTATOR_OPTION = option(key("spectator"), true);
}
