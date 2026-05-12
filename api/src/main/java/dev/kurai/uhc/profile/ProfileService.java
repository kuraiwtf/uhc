package dev.kurai.uhc.profile;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ProfileService {

  Profile getOrCreateProfile(final UUID uniqueId);

  default Profile getOrCreateProfile(final Player player) {
    return this.getOrCreateProfile(player.getUniqueId());
  }

  Optional<@NotNull Profile> getProfile(final @NotNull String name);

  Optional<@NotNull Profile> getProfile(final Predicate<@NotNull Profile> filter);

  Collection<@NotNull Profile> getProfiles();

  Collection<@NotNull Profile> getProfiles(final Predicate<@NotNull Profile> filter);
}
