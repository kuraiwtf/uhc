package dev.kurai.uhc.game.scenario;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractScenario implements Identifiable<String>, Nameable<String> {

  protected final String id;
  protected final String name;

  protected final UltraHardcoreAPI ultraHardcore;

  protected final ScenarioCategory category;

  private final Map<String, ScenarioConfiguration<?>> configurations;

  private boolean enabled;

  public AbstractScenario(
      final String id,
      final String name,
      final UltraHardcoreAPI ultraHardcore,
      final ScenarioCategory category) {
    this.category = category;
    this.configurations = Maps.newHashMap();

    this.id = id;
    this.name = name;

    this.ultraHardcore = ultraHardcore;
  }

  public abstract ItemStack provideIcon();

  public final void registerConfiguration(final ScenarioConfiguration<?> configuration) {
    this.configurations.put(configuration.getId(), configuration);
  }

  public void onEnable() {}

  public void onDisable() {}

  @Override
  public final String getId() {
    return this.id;
  }

  @Override
  public final String getName() {
    return this.name;
  }

  public ScenarioCategory getCategory() {
    return this.category;
  }

  public final boolean isEnabled() {
    return this.enabled;
  }

  public final void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }
}
