package dev.kurai.uhc.effect;

import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;

public enum EffectType {
    STRENGTH(PotionTypes.STRENGTH),
    SPEED(PotionTypes.SPEED),
    RESISTANCE(PotionTypes.RESISTANCE),
    ;

    private final PotionType potionType;

    EffectType(final PotionType potionType) {
        this.potionType = potionType;
    }

    public PotionType potionType() {
        return this.potionType;
    }
}
