package dev.kurai.uhc.effect;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.kurai.uhc.effect.event.*;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public final class EffectHolderImpl implements EffectHolder {

    private final UUID uniqueId;

    private final Table<Key, EffectType, Effect> effects;
    private final Collection<Effect> effectsView;

    public EffectHolderImpl(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.effectsView = Collections.unmodifiableCollection((this.effects = HashBasedTable.create()).values());
    }

    @Override
    public Collection<Effect> effects() {
        return this.effectsView;
    }

    @Override
    public Collection<Effect> findEffectsByKey(final Key key) {
        return this.effects
                .row(requireNonNull(key, "Effect key must not be null"))
                .values();
    }

    @Override
    public Collection<Effect> findEffectsByType(final EffectType effectType) {
        return this.effects
                .column(requireNonNull(effectType, "Effect type must not be null"))
                .values();
    }

    @Override
    public void validateEffects() {
        for (final Effect effect : this.effects.values()) {
            if (effect.expired()) {
                this.removeEffect(effect.key());
            }
        }

        Bukkit.getPluginManager().callEvent(new EffectValidateEvent(this.uniqueId));
    }

    @Override
    public void addEffect(final Effect effect) {
        final EffectPreAddEvent preAddEvent = new EffectPreAddEvent(this.uniqueId, effect);
        Bukkit.getPluginManager().callEvent(preAddEvent);
        if (preAddEvent.isCancelled()) {
            return;
        }

        this.effects.put(effect.key(), effect.effectType(), effect);
        Bukkit.getPluginManager().callEvent(new EffectPostAddEvent(this.uniqueId, effect));
    }

    @Override
    public void removeEffect(final Key key, final EffectType effectType) {
        final Effect effect = this.effects.get(key, effectType);
        if (effect == null) {
            return;
        }

        final EffectPreRemoveEvent preRemoveEvent = new EffectPreRemoveEvent(this.uniqueId, effect);
        Bukkit.getPluginManager().callEvent(preRemoveEvent);
        if (preRemoveEvent.isCancelled()) {
            return;
        }

        this.effects.remove(key, effectType);
        Bukkit.getPluginManager().callEvent(new EffectPostRemoveEvent(this.uniqueId, effect));
    }

    @Override
    public void removeEffect(final Key key) {
        for (final EffectType effectType : EffectType.values()) {
            this.removeEffect(key, effectType);
        }
    }

    @Override
    public double getEffectValue(final EffectType effectType) {
        return this.effectsView.stream()
                .filter(effect -> effect.effectType() == effectType)
                .mapToDouble(Effect::value)
                .sum();
    }
}
