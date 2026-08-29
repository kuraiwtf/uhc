package dev.kurai.uhc.effect;

import com.google.common.collect.Maps;
import java.util.Map;

public final class EffectServiceImpl implements EffectService {

  private final Map<EffectType, Double> effectValuePerLevel;

  public EffectServiceImpl() {
    this.effectValuePerLevel = Maps.newHashMap();
    this.effectValuePerLevel(EffectType.STRENGTH, 0.15);
    this.effectValuePerLevel(EffectType.SPEED, 0.2);
    this.effectValuePerLevel(EffectType.RESISTANCE, 0.2);
  }

  @Override
  public double effectValuePerLevel(final EffectType effectType) {
    return this.effectValuePerLevel.getOrDefault(effectType, 0.0);
  }

  @Override
  public void effectValuePerLevel(final EffectType effectType, final double value) {
    this.effectValuePerLevel.put(effectType, value);
  }
}
