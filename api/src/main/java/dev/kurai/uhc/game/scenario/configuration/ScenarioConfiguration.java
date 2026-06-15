package dev.kurai.uhc.game.scenario.configuration;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.value.impl.MutableValuable;

public class ScenarioConfiguration<T> implements Identifiable<String>, MutableValuable<T> {

  private final String id;
  private T value;

  public ScenarioConfiguration(final String id, final T value) {
    this.id = id;
    this.value = value;
  }

  @Override
  public final String getId() {
    return this.id;
  }

  @Override
  public final T getValue() {
    return this.value;
  }

  @Override
  public final void setValue(final T value) {
    this.value = value;
  }
}
