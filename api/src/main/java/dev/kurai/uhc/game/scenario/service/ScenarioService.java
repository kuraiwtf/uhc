package dev.kurai.uhc.game.scenario.service;

import dev.kurai.uhc.game.scenario.AbstractScenario;
import java.util.Collection;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

public interface ScenarioService {

  Collection<@NotNull AbstractScenario> getScenarios(
      final @NotNull Predicate<@NotNull AbstractScenario> filter);

  default Collection<@NotNull AbstractScenario> getScenarios() {
    return this.getScenarios(scenario -> true);
  }

  default Collection<@NotNull AbstractScenario> getEnabledScenarios() {
    return this.getScenarios(AbstractScenario::isEnabled);
  }

  void registerScenario(final @NotNull AbstractScenario scenario);

  default void registerScenarios(final AbstractScenario @NotNull ... scenarios) {
    for (final var scenario : scenarios) {
      this.registerScenario(scenario);
    }
  }

  boolean isRegistered(final @NotNull String scenarioId);

  boolean isEnabled(final @NotNull String scenarioId);
}
