package dev.kurai.uhc.game.scenario;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractScenario
    implements Identifiable<@NotNull String>, Nameable<@NotNull String> {

  private final Map<@NotNull String, @NotNull ScenarioConfiguration<?>> configurations;

  protected final String id;
  protected final String name;

  protected final UltraHardcoreAPI ultraHardcore;

  private boolean enabled;

  public AbstractScenario(
      final @NotNull String id,
      final @NotNull String name,
      final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.configurations = Maps.newHashMap();

    this.id = id;
    this.name = name;

    this.ultraHardcore = ultraHardcore;
  }

  public abstract @NotNull ItemStack provideIcon();

  public final void registerConfiguration(final @NotNull ScenarioConfiguration<?> configuration) {
    this.configurations.put(configuration.getId(), configuration);
  }

  public void onEnable() {}

  public void onDisable() {}

  @Override
  public final @NotNull String getId() {
    return this.id;
  }

  @Override
  public final @NotNull String getName() {
    return this.name;
  }

  public final boolean isEnabled() {
    return this.enabled;
  }

  public final void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }
}
