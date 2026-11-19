package dev.kurai.uhc.event.defaults.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.ecs.entity.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public final class ComponentAddEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Entity<?> entity;
    private final Component component;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
