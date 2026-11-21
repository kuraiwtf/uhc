package dev.kurai.uhc.event.defaults.power.cooldown;

import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public final class PowerCooldownDecrementEvent extends PowerCooldownEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private boolean cancelled;

    public PowerCooldownDecrementEvent(final Player player, final CooldownPowerRestriction restriction) {
        super(player, restriction);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
