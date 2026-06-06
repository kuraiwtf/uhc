package dev.kurai.uhc.profile.state;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.api.state.entity.EntityState;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
public abstract class ProfileState implements EntityState<Profile> {

  protected final String id;

  public ProfileState(final @NotNull String id) {
    this.id = id;
  }

  @Override
  public abstract void onEntry(final Profile profile);

  @Override
  public abstract void onExit(final Profile profile);

  @Override
  public @NotNull String getId() {
    return this.id;
  }
}
