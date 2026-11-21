package dev.kurai.uhc.event.defaults.profile;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.state.ProfileState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public final class ProfileStateChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Profile profile;
    private final ProfileState previousState;
    private final ProfileState newState;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
