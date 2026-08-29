package dev.kurai.uhc.effect;

import java.util.Collection;

public interface EffectHolder {

  /**
   * Retrieves the collection of active effects associated with the implementing entity. Each effect
   * in the collection represents a specific modification or influence, defined by its type, value,
   * and duration.
   *
   * @return a collection of {@code Effect} objects currently applied. The collection may be empty
   *     if no active effects exist.
   */
  Collection<Effect> effects();

  /**
   * Validates and updates the collection of effects held by the implementing entity. This method
   * will iterate over all currently active effects and remove any that have expired. An effect is
   * considered expired if its duration has passed relative to its creation time.
   *
   * <p>This operation ensures that the collection of effects remains up-to-date, preventing expired
   * effects from lingering and potentially causing unintended behavior.
   */
  void validateEffects();

  /**
   * Adds a new effect to the implementing entity's collection of effects. The effect represents a
   * modification or influence that will be applied based on its type, value, and duration.
   *
   * @param effect the {@code Effect} to be added. Must not be null. The provided effect should
   *     contain a key, type, value, duration, and creation time.
   */
  void addEffect(final Effect effect);

  /**
   * Removes an active effect associated with the given key from the implementing entity's
   * collection of effects. If no effect matches the provided key, this method will have no impact.
   * The key serves as a unique identifier for the effect to be removed.
   *
   * @param key the unique identifier of the effect to be removed. Must not be null. It should match
   *     the {@code key} of an effect in the collection to successfully remove it.
   */
  void removeEffect(final String key);

  /**
   * Retrieves the cumulative value of all active effects of the specified type associated with the
   * implementing entity. This includes summing the values of all effects of the given type that are
   * currently active and not expired.
   *
   * @param effectType the type of effect to retrieve the cumulative value for. Must not be null.
   *     Valid values are defined in the {@code EffectType} enumeration.
   * @return the total value of all active effects matching the specified type. Returns 0 if no
   *     effects of the given type are active.
   */
  double getEffectValue(final EffectType effectType);
}
