package dev.kurai.uhc.game.scenario;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.defaults.*;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class ScenarioServiceImpl implements ScenarioService {

  private final Map<@NotNull String, @NotNull AbstractScenario> scenarios;

  public ScenarioServiceImpl(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.scenarios = Maps.newHashMap();
    this.registerScenarios(
        new ArmorReplaceScenario(ultraHardcore),
        new BetaZombieScenario(ultraHardcore),
        new CobblestoneOnlyScenario(ultraHardcore),
        new CutCleanScenario(ultraHardcore),
        new HasteyBoysScenario(ultraHardcore),
        new NoFoodScenario(ultraHardcore),
        new SafeMinersScenario(ultraHardcore),
        new TimberScenario(ultraHardcore));
  }

  @Override
  public @NotNull @Unmodifiable Collection<@NotNull AbstractScenario> getScenarios(
      final @NotNull Predicate<@NotNull AbstractScenario> filter) {
    return this.scenarios.values().stream().filter(filter).toList();
  }

  @Override
  public void registerScenario(final @NotNull AbstractScenario scenario) {
    this.scenarios.put(scenario.getId(), scenario);
  }

  @Override
  public boolean isRegistered(final @NotNull String scenarioId) {
    return this.scenarios.containsKey(scenarioId);
  }

  @Override
  public boolean isEnabled(final @NotNull String scenarioId) {
    return this.scenarios.containsKey(scenarioId) && this.scenarios.get(scenarioId).isEnabled();
  }
}
