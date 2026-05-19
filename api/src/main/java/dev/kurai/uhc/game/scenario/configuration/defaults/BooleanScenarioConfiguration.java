package dev.kurai.uhc.game.scenario.configuration.defaults;

import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import org.jetbrains.annotations.NotNull;

public final class BooleanScenarioConfiguration extends ScenarioConfiguration<@NotNull Boolean> {

  public BooleanScenarioConfiguration(final @NotNull String id) {
    super(id, true);
  }

  public BooleanScenarioConfiguration(final @NotNull String id, final boolean value) {
    super(id, value);
  }
}
