package dev.kurai.uhc.profile.state;

import dev.kurai.uhc.profile.Profile;

public final class DeadProfileState extends ProfileState {

  public DeadProfileState() {
    super("dead");
  }

  @Override
  public void onEntry(final Profile profile) {}

  @Override
  public void onExit(final Profile profile) {}
}
