package dev.kurai.uhc.ecs.component.defaults;

import dev.kurai.uhc.ecs.component.Component;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public final class NameComponent implements Component {

  private Supplier<String> name;

  public NameComponent() {}

  public NameComponent(final @NotNull Supplier<String> name) {
    this.name = name;
  }

  public String getName() {
    return this.name.get();
  }

  public void setName(final Supplier<String> name) {
    this.name = name;
  }
}
