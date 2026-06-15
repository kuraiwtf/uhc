package dev.kurai.uhc.module.camp;

import dev.kurai.uhc.util.api.Identifiable;

public abstract class AbstractCampData<T extends AbstractCamp<?>>
    implements Identifiable<Class<? extends T>> {

  protected final Class<? extends T> identifier;

  protected AbstractCampData(final Class<? extends T> team) {
    this.identifier = team;
  }

  @Override
  public Class<? extends T> getId() {
    return this.identifier;
  }
}
