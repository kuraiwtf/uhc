package dev.kurai.uhc.profile.service;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileImpl;
import java.util.*;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class ProfileServiceImpl implements ProfileService {

  private final Map<@NotNull UUID, @NotNull Profile> profiles;

  private final UltraHardcoreAPI ultraHardcore;

  public ProfileServiceImpl(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.profiles = Maps.newHashMap();
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public @NotNull Profile createProfile(final @NotNull UUID id, final @NotNull String name) {
    return this.profiles.computeIfAbsent(
        id, uuid -> new ProfileImpl(uuid, name, this.ultraHardcore));
  }

  @Override
  public @Nullable Profile getProfile(final @NotNull UUID id) {
    return this.profiles.get(id);
  }

  @Override
  public @NotNull Optional<@NotNull Profile> getProfile(final @NotNull String name) {
    return this.getProfile(profile -> profile.getName().equals(name));
  }

  @Override
  public @NotNull Optional<@NotNull Profile> getProfile(final Predicate<@NotNull Profile> filter) {
    return this.profiles.values().stream().filter(filter).findFirst();
  }

  @Contract(pure = true)
  @Override
  public @NotNull Collection<@NotNull Profile> getProfiles() {
    return this.profiles.values();
  }

  @Override
  public @NotNull @Unmodifiable Collection<@NotNull Profile> getProfiles(
      final Predicate<@NotNull Profile> filter) {
    return this.profiles.values().stream().filter(filter).toList();
  }
}
