package dev.kurai.uhc.event.defaults.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.ecs.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@EqualsAndHashCode(callSuper = true)
@Data
public final class ComponentRemoveEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final Entity<?> entity;
    private final Component component;

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
