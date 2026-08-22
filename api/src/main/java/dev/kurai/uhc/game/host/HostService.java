package dev.kurai.uhc.game.host;

import dev.kurai.uhc.profile.Profile;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface HostService {

  @Nullable Profile hostProfile();

  @Nullable UUID host();

  void host(final @Nullable UUID host);

  void processHostResolver(final HostResolver resolver);

  Collection<TickableHostResolver> tickableHostResolvers();

  void addTickableHostResolver(final TickableHostResolver resolver);

  void removeTickableHostResolver(final TickableHostResolver resolver);

  Set<UUID> coHosts();

  boolean addCoHost(final UUID coHost);

  boolean removeCoHost(final UUID coHost);

  boolean coHost(final UUID uniqueId);

  default boolean coHost(final Player player) {
    return this.coHost(player.getUniqueId());
  }

  boolean isHost(final UUID uniqueId);

  default boolean isHost(final Player player) {
    return this.isHost(player.getUniqueId());
  }

  boolean hasHostAccess(final UUID uniqueId);

  default boolean hasHostAccess(final Player player) {
    return this.hasHostAccess(player.getUniqueId());
  }
}
