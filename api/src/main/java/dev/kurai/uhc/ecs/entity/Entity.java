package dev.kurai.uhc.ecs.entity;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.util.api.Identifiable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Entity<I> extends Identifiable<I> {

  <E extends Entity<I>> E addComponent(final Component component);

  default <E extends Entity<I>> E addComponents(final Component... components) {
    for (final Component component : components) {
      this.addComponent(component);
    }
    return (E) this;
  }

  boolean removeComponent(final Class<? extends Component> componentClass);

  boolean hasComponent(final Class<? extends Component> componentClass);

  <T extends Component> T getComponent(final Class<T> componentClass);
}
