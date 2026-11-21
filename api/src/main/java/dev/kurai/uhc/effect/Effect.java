package dev.kurai.uhc.effect;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.time.Duration;
import java.time.Instant;

import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;

public record Effect(Key key, EffectType effectType, double value, Duration duration, Instant creationTime)
        implements Keyed {

    public Effect {
        requireNonNull(key, "Effect key must not be null");
        requireNonNull(effectType, "Effect type must not be null");
        requireNonNull(duration, "Effect duration must not be null");
        requireNonNull(creationTime, "Effect creation time must not be null");
    }

    public Effect(final Key key, final EffectType effectType, final double value, final Duration duration) {
        this(key, effectType, value, duration, now());
    }

    public boolean expired() {
        return !this.duration.isZero() && now().isAfter(this.creationTime.plus(this.duration));
    }
}
