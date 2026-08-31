package dev.kurai.uhc.skin;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.SkinModificationComponent;

final class SkinUpdaterTask implements Runnable {

  private final ProfileService profileService;
  private final SkinService skinService;

  SkinUpdaterTask(final ProfileService profileService, final SkinService skinService) {
    this.profileService = profileService;
    this.skinService = skinService;
  }

  @Override
  public void run() {
    for (final Profile profile :
        this.profileService.getProfiles(
            profile -> profile.hasComponent(SkinModificationComponent.class))) {
      if (profile.findPlayer().isPresent()) {
        this.skinService.holder(profile.getId()).updateSkin();
        profile.removeComponent(SkinModificationComponent.class);
      }
    }
  }
}
