package dev.kurai.uhc.effect.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class EffectValidateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final UUID uniqueId;

    public EffectValidateEvent(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public UUID uniqueId() {
        return this.uniqueId;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
