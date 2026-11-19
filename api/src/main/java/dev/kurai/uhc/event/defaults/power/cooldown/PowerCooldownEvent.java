package dev.kurai.uhc.event.defaults.power.cooldown;

import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class PowerCooldownEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final @Nullable Player player;
    private final CooldownPowerRestriction restriction;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
