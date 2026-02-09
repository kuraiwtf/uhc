package dev.kurai.uhc.game.scenario.configuration.defaults;

import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import org.jetbrains.annotations.NotNull;

public final class IntegerScenarioConfiguration extends ScenarioConfiguration<@NotNull Integer> {

  public IntegerScenarioConfiguration(final @NotNull String id) {
    super(id, 0);
  }

  public IntegerScenarioConfiguration(final @NotNull String id, final int value) {
    super(id, value);
  }
}
