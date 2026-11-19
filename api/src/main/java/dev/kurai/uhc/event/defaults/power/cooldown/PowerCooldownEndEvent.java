package dev.kurai.uhc.event.defaults.power.cooldown;

import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public final class PowerCooldownEndEvent extends PowerCooldownEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PowerCooldownEndEvent(final Player player, final CooldownPowerRestriction restriction) {
        super(player, restriction);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
