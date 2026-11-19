package dev.kurai.uhc.game.combat;

import dev.kurai.uhc.profile.Profile;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CombatService {

    long combatDuration();

    void combatDuration(final long millis);

    void enterCombat(final Profile profile);

    void leaveCombat(final Profile profile);

    boolean inCombat(final Profile profile);

    void validate(final Profile profile);
}
