package dev.kurai.uhc.effect;

import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;

public final class EffectValidatorTask implements Runnable {

  private final ProfileService profileService;

  public EffectValidatorTask(final ProfileService profileService) {
    this.profileService = profileService;
  }

  @Override
  public void run() {
    for (final Profile profile : this.profileService.getPlayingProfiles()) {
      final EffectHoldingComponent component = profile.getComponent(EffectHoldingComponent.class);
      if (component != null) {
        component.holder().validateEffects();
      }
    }
  }
}
