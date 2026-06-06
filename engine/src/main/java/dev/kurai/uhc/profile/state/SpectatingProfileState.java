package dev.kurai.uhc.profile.state;

import dev.kurai.uhc.profile.Profile;

public final class SpectatingProfileState extends ProfileState {

  public SpectatingProfileState() {
    super("spectating");
  }

  @Override
  public void onEntry(final Profile profile) {}

  @Override
  public void onExit(final Profile profile) {}
}
