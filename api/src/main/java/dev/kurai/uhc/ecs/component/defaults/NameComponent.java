package dev.kurai.uhc.ecs.component.defaults;

import dev.kurai.uhc.ecs.component.Component;

public final class NameComponent implements Component {

  private String name;

  public NameComponent() {}

  public NameComponent(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
