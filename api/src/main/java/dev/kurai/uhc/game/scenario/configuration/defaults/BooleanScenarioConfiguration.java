package dev.kurai.uhc.game.scenario.configuration.defaults;

import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import org.jetbrains.annotations.NotNull;

public final class BooleanScenarioConfiguration extends ScenarioConfiguration< Boolean> {

  public BooleanScenarioConfiguration(final  String id) {
    super(id, true);
  }

  public BooleanScenarioConfiguration(final  String id, final boolean value) {
    super(id, value);
  }
}
