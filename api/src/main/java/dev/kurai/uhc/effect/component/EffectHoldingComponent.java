package dev.kurai.uhc.effect.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.effect.EffectHolder;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public record EffectHoldingComponent(EffectHolder holder) implements Component {

    public EffectHoldingComponent {
        Objects.requireNonNull(holder, "Effect holder must not be null");
    }
}
