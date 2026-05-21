package dev.kurai.uhc.game.host;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public interface HostService {

  UUID host();

  void host(final UUID host);

  void processHostResolver(final HostResolver resolver);

  Collection<TickableHostResolver> tickableHostResolvers();

  void addTickableHostResolver(final TickableHostResolver resolver);

  void removeTickableHostResolver(final TickableHostResolver resolver);

  Set<UUID> coHosts();

  void addCoHost(final UUID coHost);

  void removeCoHost(final UUID coHost);

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
