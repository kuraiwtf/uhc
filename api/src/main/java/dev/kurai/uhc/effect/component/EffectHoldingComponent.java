package dev.kurai.uhc.effect.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.effect.EffectHolder;

public record EffectHoldingComponent(EffectHolder holder) implements Component {}
