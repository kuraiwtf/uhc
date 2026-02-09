package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import java.util.UUID;

public final class DeadComponent implements Component {

  private UUID killer;
  private long deathTime;

  public DeadComponent() {}

  public DeadComponent(final UUID killer, final long deathTime) {
    this.killer = killer;
    this.deathTime = deathTime;
  }

  public UUID getKiller() {
    return this.killer;
  }

  public void setKiller(final UUID killer) {
    this.killer = killer;
  }

  public long getDeathTime() {
    return this.deathTime;
  }

  public void setDeathTime(final long deathTime) {
    this.deathTime = deathTime;
  }
}
