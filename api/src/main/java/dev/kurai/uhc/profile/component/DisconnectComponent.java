package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DisconnectComponent implements Component {

  private long timeLeft;
  private Instant lastLogin;

  public DisconnectComponent(final long timeLeft, final Instant lastLogin) {
    this.timeLeft = timeLeft;
    this.lastLogin = lastLogin;
  }
}
