package dev.kurai.uhc.ecs.entity;

import dev.kurai.uhc.ecs.component.Component;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public interface EntityBridge<I> {

    default Collection<Component> getComponents() {
        return this.asEntity().getComponents();
    }

    default Entity<I> addComponent(final Component component) {
        return this.asEntity().addComponent(component);
    }

    default Entity<I> addComponents(final Component... components) {
        return this.asEntity().addComponents(components);
    }

    default boolean removeComponent(final Class<? extends Component> componentClass) {
        return this.asEntity().removeComponent(componentClass);
    }

    default boolean hasComponent(final Class<? extends Component> componentClass) {
        return this.asEntity().hasComponent(componentClass);
    }

    default <C extends Component> @Nullable C getComponent(final Class<C> componentClass) {
        return this.asEntity().getComponent(componentClass);
    }

    Entity<I> asEntity();
}
