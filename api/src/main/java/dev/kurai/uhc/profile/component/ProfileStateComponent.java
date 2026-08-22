package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.profile.state.ProfileState;

public final class ProfileStateComponent implements Component {

  private ProfileState state;

  public ProfileStateComponent() {}

  public ProfileStateComponent(final ProfileState state) {
    this.state = state;
  }

  public ProfileState getState() {
    return this.state;
  }

  public void setState(final ProfileState state) {
    this.state = state;
  }
}
