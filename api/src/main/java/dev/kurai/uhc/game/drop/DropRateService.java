package dev.kurai.uhc.game.drop;

import org.jspecify.annotations.Nullable;

import java.util.Collection;

public interface DropRateService {

    Collection<AbstractDropRateModifier> getModifiers();

    void registerModifier(AbstractDropRateModifier modifier);

    default void registerModifiers(final AbstractDropRateModifier... modifiers) {
        for (final var modifier : modifiers) {
            this.registerModifier(modifier);
        }
    }

    void unregisterModifier(String id);

    default void unregisterModifiers(final String... ids) {
        for (final var id : ids) {
            this.unregisterModifier(id);
        }
    }

    @Nullable
    AbstractDropRateModifier getModifier(final String id);
}
