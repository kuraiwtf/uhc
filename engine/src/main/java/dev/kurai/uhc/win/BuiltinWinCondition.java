package dev.kurai.uhc.win;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public final class BuiltinWinCondition implements WinCondition {

  private final ProfileService profileService;

  public BuiltinWinCondition(final ProfileService profileService) {
    this.profileService = profileService;
  }

  @Override
  public @Nullable @Unmodifiable Collection<Profile> validateWin() {
    final var playingProfiles = this.profileService.getPlayingProfiles();
    if (playingProfiles.size() > 1) {
      return null;
    }

    return Collections.singleton(playingProfiles.iterator().next());
  }
}
