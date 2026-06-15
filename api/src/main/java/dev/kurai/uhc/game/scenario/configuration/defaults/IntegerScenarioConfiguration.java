package dev.kurai.uhc.game.scenario.configuration.defaults;

import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;

public final class IntegerScenarioConfiguration extends ScenarioConfiguration<Integer> {

  public IntegerScenarioConfiguration(final String id) {
    super(id, 0);
  }

  public IntegerScenarioConfiguration(final String id, final int value) {
    super(id, value);
  }
}
