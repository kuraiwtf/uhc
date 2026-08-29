package dev.kurai.uhc.effect;

import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;

public enum EffectType {
  STRENGTH(PotionTypes.STRENGTH),
  SPEED(PotionTypes.SPEED),
  RESISTANCE(PotionTypes.RESISTANCE),
  ;

  private final PotionType packetType;

  EffectType(final PotionType packetType) {
    this.packetType = packetType;
  }

  public PotionType packetType() {
    return this.packetType;
  }
}
