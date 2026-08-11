package dev.kurai.uhc.game.scenario;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.defaults.*;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;
import org.jetbrains.annotations.Unmodifiable;

public final class ScenarioServiceImpl implements ScenarioService {

  private final Map<String, AbstractScenario> scenarios;

  public ScenarioServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.scenarios = Maps.newHashMap();
    this.registerScenarios(
        new ArmorReplaceScenario(ultraHardcore),
        new BatsScenario(ultraHardcore),
        new BetaZombieScenario(ultraHardcore),
        new CobblestoneOnlyScenario(ultraHardcore),
        new CutCleanScenario(ultraHardcore),
        new HasteyBoysScenario(ultraHardcore),
        new MeleeFunScenario(ultraHardcore),
        new NoFallScenario(ultraHardcore),
        new NoFoodScenario(ultraHardcore),
        new SafeMinersScenario(ultraHardcore),
        new TimberScenario(ultraHardcore));
  }

  @Override
  public @Unmodifiable Collection<AbstractScenario> getScenarios(
      final Predicate<AbstractScenario> filter) {
    return this.scenarios.values().stream().filter(filter).toList();
  }

  @Override
  public void registerScenario(final AbstractScenario scenario) {
    this.scenarios.put(scenario.getId(), scenario);
  }

  @Override
  public boolean isRegistered(final String scenarioId) {
    return this.scenarios.containsKey(scenarioId);
  }

  @Override
  public boolean isEnabled(final String scenarioId) {
    return this.scenarios.containsKey(scenarioId) && this.scenarios.get(scenarioId).isEnabled();
  }
}
