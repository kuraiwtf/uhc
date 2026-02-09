package dev.kurai.uhc.profile.state;

import dev.kurai.uhc.profile.Profile;

public final class WaitingProfileState extends ProfileState {

  public WaitingProfileState() {
    super("waiting");
  }

  @Override
  public void onEntry(final Profile profile) {

  }

  @Override
  public void onExit(final Profile profile) {

  }
}
