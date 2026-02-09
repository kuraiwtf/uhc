package dev.kurai.uhc.game.scenario.configuration;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.value.impl.MutableValuable;
import org.jetbrains.annotations.NotNull;

public class ScenarioConfiguration<T> implements Identifiable<@NotNull String>, MutableValuable<T> {

  private final String id;
  private T value;

  public ScenarioConfiguration(final String id, final T value) {
    this.id = id;
    this.value = value;
  }

  public final @NotNull String getId() {
    return this.id;
  }

  public final T getValue() {
    return value;
  }

  public final void setValue(final T value) {
    this.value = value;
  }
}
