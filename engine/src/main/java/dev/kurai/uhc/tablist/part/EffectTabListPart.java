package dev.kurai.uhc.tablist.part;

import dev.kurai.uhc.effect.EffectHolder;
import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.tablist.TabListPart;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import static dev.kurai.uhc.effect.EffectType.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class EffectTabListPart implements TabListPart {

    public static final String PART_KEY = "effect";

    private static final Component SEPARATOR = text().appendSpace()
            .append(text(CC.SQUARE, DARK_GRAY))
            .appendSpace()
            .build();

    private final ProfileService participantService;

    @Contract(pure = true)
    public EffectTabListPart(final ProfileService participantService) {
        this.participantService = participantService;
    }

    @Contract(pure = true)
    @Override
    public String key() {
        return PART_KEY;
    }

    @Override
    public int priority() {
        return -50;
    }

    @Contract("_ -> new")
    @Override
    public @Unmodifiable Component render(final Player player) {
        final Profile profile = this.participantService.getOrCreateProfile(player.getUniqueId());
        final EffectHoldingComponent effectComponent = profile.getComponent(EffectHoldingComponent.class);
        assert effectComponent != null;
        final EffectHolder effectHolder = effectComponent.holder();
        return text().append(this.provideEffectComponent(effectHolder.effectValue(STRENGTH), RED, '⚔'))
                .append(SEPARATOR)
                .append(this.provideEffectComponent(effectHolder.effectValue(RESISTANCE), GREEN, '☘'))
                .append(SEPARATOR)
                .append(this.provideEffectComponent(effectHolder.effectValue(SPEED), AQUA, '✪'))
                .appendNewline()
                .build();
    }

    @Contract(pure = true)
    @Override
    public Position position() {
        return Position.BOTTOM;
    }

    @Contract("_, _, _ -> new")
    private Component provideEffectComponent(final double value, final TextColor color, final char symbol) {
        return text().append(text("%.1f".formatted(value * 100), color))
                .append(text('%', color))
                .appendSpace()
                .append(text(symbol, color))
                .build();
    }
}
