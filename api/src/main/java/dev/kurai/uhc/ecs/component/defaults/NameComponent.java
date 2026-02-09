package dev.kurai.uhc.ecs.component.defaults;

import dev.kurai.uhc.ecs.component.Component;
import org.jetbrains.annotations.NotNull;

public final class NameComponent implements Component {

  private String name;

  public NameComponent() {}

  public NameComponent(final @NotNull String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
