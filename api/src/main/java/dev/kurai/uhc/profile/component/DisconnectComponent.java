package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public final class DisconnectComponent implements Component {

    private int timeLeft;
    private Instant lastLogin;

    public DisconnectComponent(final int timeLeft, final Instant lastLogin) {
        this.timeLeft = timeLeft;
        this.lastLogin = lastLogin;
    }
}
