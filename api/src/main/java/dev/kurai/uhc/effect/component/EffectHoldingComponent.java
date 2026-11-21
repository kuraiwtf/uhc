package dev.kurai.uhc.effect.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.effect.EffectHolder;

import java.util.Objects;

public record EffectHoldingComponent(EffectHolder holder) implements Component {

    public EffectHoldingComponent {
        Objects.requireNonNull(holder, "Effect holder must not be null");
    }
}
