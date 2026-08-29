package dev.kurai.uhc.effect;

public interface EffectService {

  double effectValuePerLevel(final EffectType effectType);

  void effectValuePerLevel(final EffectType effectType, final double value);
}
