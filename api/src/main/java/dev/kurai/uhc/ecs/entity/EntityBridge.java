package dev.kurai.uhc.ecs.entity;

import dev.kurai.uhc.ecs.component.Component;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public interface EntityBridge<I, S extends EntityBridge<I, S>> {

    default Collection<Component> getComponents() {
        return this.asEntity().getComponents();
    }

    default S addComponent(final Component component) {
        this.asEntity().addComponent(component);
        return this.self();
    }

    default S addComponents(final Component... components) {
        this.asEntity().addComponents(components);
        return this.self();
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

    @SuppressWarnings("unchecked")
    private S self() {
        return (S) this;
    }
}
