package dev.kurai.uhc.game.configuration.game;

import static dev.kurai.uhc.util.api.option.Option.option;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.util.api.option.Option;

public final class GameConfiguration {

  private GameConfiguration() {
    /* This utility class should not be instantiated */
  }

  public static final Option<Boolean> SPECTATOR_OPTION = option(key("spectator"), true);
}
