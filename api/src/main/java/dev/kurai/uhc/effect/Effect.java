package dev.kurai.uhc.effect;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.time.Duration;
import java.time.Instant;

import static java.time.Instant.now;

public final class Effect implements Keyed {

    private final Key key;
    private final EffectType type;
    private final double value;
    private final Duration duration;
    private final Instant creationTime;

    public Effect(final Key key, final EffectType type, final double value, final Duration duration) {
        this.key = key;
        this.type = type;
        this.value = value;
        this.duration = duration;
        this.creationTime = now();
    }

    @Override
    public Key key() {
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
