package dev.kurai.uhc.profile;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import java.util.*;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileServiceImpl implements ProfileService {

  private final Map<UUID, Profile> profiles;

  private final Collection<Profile> profilesView;

  private final UltraHardcoreAPI ultraHardcore;

  public ProfileServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.profiles = Maps.newHashMap();
    this.profilesView = Collections.unmodifiableCollection(this.profiles.values());

    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public Profile getOrCreateProfile(final UUID uniqueId) {
    return this.profiles.computeIfAbsent(
        uniqueId, uuid -> new ProfileImpl(uuid, this.ultraHardcore));
  }

  @Override
  public Optional<Profile> getProfile(final String name) {
    return this.getProfile(profile -> profile.getName().equals(name));
  }

  @Override
  public Optional<Profile> getProfile(final Predicate<Profile> filter) {
    return this.profilesView.stream().filter(filter).findFirst();
  }

  @Contract(pure = true)
  @Override
  public Collection<Profile> getProfiles() {
    return this.profilesView;
  }

  @Override
  public @Unmodifiable Collection<Profile> getProfiles(final Predicate<Profile> filter) {
    return this.profilesView.stream().filter(filter).toList();
  }

  @Override
  public @Unmodifiable Collection<Profile> getPlayingProfiles() {
    return this.getProfiles(profile -> !profile.hasComponent(SpectatorComponent.class));
  }
}
