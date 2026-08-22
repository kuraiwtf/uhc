package dev.kurai.uhc.profile;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ProfileService {

  Profile getOrCreateProfile(final UUID uniqueId);

  default Profile getOrCreateProfile(final Player player) {
    return this.getOrCreateProfile(player.getUniqueId());
  }

  Optional<Profile> getProfile(final String name);

  Optional<Profile> getProfile(final Predicate<Profile> filter);

  Collection<Profile> getProfiles();

  Collection<Profile> getProfiles(final Predicate<Profile> filter);

  Collection<Profile> getPlayingProfiles();
}
