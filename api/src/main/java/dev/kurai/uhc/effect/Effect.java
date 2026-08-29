package dev.kurai.uhc.effect;

import static java.time.Instant.now;

import java.time.Duration;
import java.time.Instant;

public final class Effect {

  private final String key;
  private final EffectType type;
  private final double value;
  private final Duration duration;
  private final Instant creationTime;

  public Effect(
      final String key, final EffectType type, final double value, final Duration duration) {
    this.key = key;
    this.type = type;
    this.value = value;
    this.duration = duration;
    this.creationTime = now();
  }

  public String key() {
    return this.key;
  }

  public EffectType type() {
    return this.type;
  }

  public double value() {
    return this.value;
  }

  public Duration duration() {
    return this.duration;
  }

  public boolean isExpired() {
    return !this.duration.isZero() && now().isAfter(this.creationTime.plus(this.duration));
  }
}
