package dev.kurai.uhc.util.api.duration.impl;

import dev.kurai.uhc.util.api.duration.Durable;
import org.jetbrains.annotations.NotNull;

public interface MutableDurable extends Durable {

  @Override
  
  Long getDuration();

  void setDuration(final  Long duration);

  @Override
  default boolean isPermanent() {
    return Durable.super.isPermanent();
  }

  @Override
  default boolean isActive() {
    return Durable.super.isActive();
  }

  @Override
  default boolean isExpired() {
    return Durable.super.isExpired();
  }

  @Override
  default long getRemaining() {
    return Durable.super.getRemaining();
  }

  @Override
  default long getExpiration() {
    return Durable.super.getExpiration();
  }

  @Override
  
  Long getCreatedAt();
}
