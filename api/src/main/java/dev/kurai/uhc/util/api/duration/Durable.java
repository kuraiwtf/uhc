package dev.kurai.uhc.util.api.duration;

import dev.kurai.uhc.util.api.Traceable;
import org.jetbrains.annotations.NotNull;

public interface Durable extends Traceable<Long> {

  
  Long getDuration();

  default boolean isPermanent() {
    return this.getDuration() == -1;
  }

  default boolean isActive() {
    return this.isPermanent()
        || System.currentTimeMillis() < this.getCreatedAt() + this.getDuration();
  }

  default boolean isExpired() {
    return !this.isActive();
  }

  default long getRemaining() {
    return this.isPermanent()
        ? -1
        : this.getDuration() - (System.currentTimeMillis() - this.getCreatedAt());
  }

  default long getExpiration() {
    return this.isPermanent() ? -1 : this.getCreatedAt() + this.getDuration();
  }
}
