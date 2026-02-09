package dev.kurai.uhc.profile.service;

import dev.kurai.uhc.profile.Profile;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ProfileService {

  default @NotNull Profile createProfile(final @NotNull Player player) {
    return this.createProfile(player.getUniqueId(), player.getName());
  }

  Profile createProfile(final @NotNull UUID id, final @NotNull String name);

  @Nullable
  Profile getProfile(final @NotNull UUID id);

  Optional<@NotNull Profile> getProfile(final @NotNull String name);

  Optional<@NotNull Profile> getProfile(final Predicate<@NotNull Profile> filter);

  Collection<@NotNull Profile> getProfiles();

  Collection<@NotNull Profile> getProfiles(final Predicate<@NotNull Profile> filter);
}
