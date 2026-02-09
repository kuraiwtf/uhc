package dev.kurai.uhc.profile.state;

import dev.kurai.uhc.profile.Profile;

public final class PlayingProfileState extends ProfileState {

  public PlayingProfileState() {
    super("playing");
  }

  @Override
  public void onEntry(final Profile profile) {}

  @Override
  public void onExit(final Profile profile) {}
}
