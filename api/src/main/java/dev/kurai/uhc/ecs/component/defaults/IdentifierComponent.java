package dev.kurai.uhc.ecs.component.defaults;

import dev.kurai.uhc.ecs.component.Component;

public class IdentifierComponent<T> implements Component {

  private T identifier;

  public IdentifierComponent() {}

  public IdentifierComponent(final T identifier) {
    this.identifier = identifier;
  }

  public T getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(final T identifier) {
    this.identifier = identifier;
  }
}
