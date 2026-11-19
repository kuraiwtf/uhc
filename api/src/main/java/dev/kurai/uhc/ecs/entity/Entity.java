package dev.kurai.uhc.ecs.entity;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.event.defaults.component.ComponentAddEvent;
import dev.kurai.uhc.event.defaults.component.ComponentRemoveEvent;
import dev.kurai.uhc.util.api.Identifiable;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class Entity<I> implements EntityBridge<I>, Identifiable<I> {

    private final EventService eventService;

    private final I identifier;

    private final Map<Class<? extends Component>, Component> components;

    public Entity(final I identifier) {
        this.eventService = UltraHardcoreAPI.getInstance().eventService();

        this.identifier = identifier;

        this.components = Maps.newHashMap();
    }

    @Override
    public I getId() {
        return this.identifier;
    }

    @Override
    public Collection<Component> getComponents() {
        return List.copyOf(this.components.values());
    }

    @Override
    public <E extends Entity<I>> E addComponent(final Component component) {
        this.components.put(component.getClass(), component);
        this.eventService.dispatchEvent(new ComponentAddEvent(this, component));
        return (E) this;
    }

    @Override
    public <E extends Entity<I>> E addComponents(final Component... components) {
        for (final Component component : components) {
            this.addComponent(component);
        }
        return (E) this;
    }

    @Override
    public boolean removeComponent(final Class<? extends Component> componentClass) {
        final Component component = this.components.remove(componentClass);
        if (component != null) {
            this.eventService.dispatchEvent(new ComponentRemoveEvent(this, component));
        }
        return component != null;
    }

    @Override
    public boolean hasComponent(final Class<? extends Component> componentClass) {
        return this.components.containsKey(componentClass);
    }

    @Override
    public <C extends Component> @Nullable C getComponent(final Class<C> componentClass) {
        if (this.components.get(componentClass) == null) {
            return null;
        }

        return componentClass.cast(this.components.get(componentClass));
    }

    @Override
    public Entity<I> asEntity() {
        return this;
    }
}
