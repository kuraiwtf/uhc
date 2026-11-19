package dev.kurai.uhc.game.combat;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;

public final class CombatValidatorTask implements Runnable {

    private final ProfileService profileService;
    private final CombatService combatService;

    public CombatValidatorTask(final ProfileService profileService, final CombatService combatService) {
        this.profileService = profileService;
        this.combatService = combatService;
    }

    @Override
    public void run() {
        for (final Profile profile : this.profileService.getProfiles()) {
            this.combatService.validate(profile);
        }
    }
}
