package dev.kurai.uhc.game.scenario;

import java.util.Collection;
import java.util.function.Predicate;

public interface ScenarioService {

  Collection<AbstractScenario> getScenarios(final Predicate<AbstractScenario> filter);

  default Collection<AbstractScenario> getScenarios() {
    return this.getScenarios(scenario -> true);
  }

  default Collection<AbstractScenario> getEnabledScenarios() {
    return this.getScenarios(AbstractScenario::isEnabled);
  }

  void registerScenario(final AbstractScenario scenario);

  default void registerScenarios(final AbstractScenario... scenarios) {
    for (final var scenario : scenarios) {
      this.registerScenario(scenario);
    }
  }

  boolean isRegistered(final String scenarioId);

  boolean isEnabled(final String scenarioId);
}
