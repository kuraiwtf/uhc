package dev.kurai.uhc.effect;

import com.google.common.collect.Maps;
import dev.kurai.uhc.effect.event.*;
import java.util.*;
import org.bukkit.Bukkit;

public final class EffectHolderImpl implements EffectHolder {

  private final UUID uniqueId;
  private final Map<String, Effect> effects;
  private final Collection<Effect> effectsView;

  public EffectHolderImpl(final UUID uniqueId) {
    this.uniqueId = uniqueId;
    this.effects = Maps.newHashMap();
    this.effectsView = Collections.unmodifiableCollection(this.effects.values());
  }

  @Override
  public Collection<Effect> effects() {
    return this.effectsView;
  }

  @Override
  public void validateEffects() {
    for (final Effect effect : this.effects.values()) {
      if (effect.isExpired()) {
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

    this.effects.put(effect.key(), effect);
    Bukkit.getPluginManager().callEvent(new EffectPostAddEvent(this.uniqueId, effect));
  }

  @Override
  public void removeEffect(final String key) {
    final Effect effect = this.effects.get(key);
    if (effect == null) {
      return;
    }

    final EffectPreRemoveEvent preRemoveEvent = new EffectPreRemoveEvent(this.uniqueId, effect);
    Bukkit.getPluginManager().callEvent(preRemoveEvent);
    if (preRemoveEvent.isCancelled()) {
      return;
    }

    this.effects.remove(key);
    Bukkit.getPluginManager().callEvent(new EffectPostRemoveEvent(this.uniqueId, effect));
  }

  @Override
  public double getEffectValue(final EffectType effectType) {
    return this.effectsView.stream()
        .filter(effect -> effect.type() == effectType)
        .mapToDouble(Effect::value)
        .sum();
  }
}
