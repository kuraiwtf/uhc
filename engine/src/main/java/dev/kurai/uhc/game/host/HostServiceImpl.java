package dev.kurai.uhc.game.host;

import com.google.common.collect.Sets;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.host.HostAccessUpdateEvent;
import dev.kurai.uhc.game.host.resolver.PlayerJoinHostResolver;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public final class HostServiceImpl implements HostService {

  private UUID host;
  private final Set<UUID> coHosts = Sets.newHashSet();
  private final Collection<TickableHostResolver> tickableHostResolvers = Sets.newHashSet();
  private final UltraHardcoreAPI ultraHardcore;

  public HostServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    final var service = HostServiceImpl.this;
    this.processHostResolver(
        () -> {
          final var env = System.getenv("SERVER_HOST_ID");
          if (env != null) {
            return UUID.fromString(env);
          }

          return null;
        });

    this.addTickableHostResolver(new PlayerJoinHostResolver(ultraHardcore.eventService()));

    new BukkitRunnable() {
      @Override
      public void run() {
        if (service.host != null) {
          this.cancel();
          return;
        }

        for (final var resolver : service.tickableHostResolvers) {
          service.processHostResolver(resolver);
          if (service.host != null) {
            this.cancel();
            return;
          }
        }
      }
    }.runTaskTimerAsynchronously(ultraHardcore.plugin(), 0L, 1L);
  }

  @Override
  public void processHostResolver(final HostResolver resolver) {
    if (this.host != null) {
      return;
    }

    final var uuid = resolver.resolveHost();
    if (uuid == null) {
      return;
    }

    this.host(uuid);
  }

  @Override
  public void host(final UUID host) {
    if (this.host != null) {
      final var event =
          this.ultraHardcore
              .eventService()
              .dispatchEvent(
                  new HostAccessUpdateEvent(
                      this.host,
                      HostAccessUpdateEvent.Type.HOST,
                      HostAccessUpdateEvent.Status.DENIED));
      if (event.isCancelled()) {
        return;
      }
    }

    final var event =
        this.ultraHardcore
            .eventService()
            .dispatchEvent(
                new HostAccessUpdateEvent(
                    host, HostAccessUpdateEvent.Type.HOST, HostAccessUpdateEvent.Status.ALLOWED));
    if (event.isCancelled()) {
      return;
    }

    this.host = host;
  }

  @Override
  public void addTickableHostResolver(final TickableHostResolver resolver) {
    this.tickableHostResolvers.add(resolver);
  }

  @Override
  public void removeTickableHostResolver(final TickableHostResolver resolver) {
    this.tickableHostResolvers.remove(resolver);
  }

  @Override
  public boolean addCoHost(final UUID coHost) {
    final var event =
        new HostAccessUpdateEvent(
            coHost, HostAccessUpdateEvent.Type.CO_HOST, HostAccessUpdateEvent.Status.ALLOWED);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      return false;
    }

    this.coHosts.add(coHost);
    return true;
  }

  @Override
  public boolean removeCoHost(final UUID coHost) {
    final var event =
        new HostAccessUpdateEvent(
            coHost, HostAccessUpdateEvent.Type.CO_HOST, HostAccessUpdateEvent.Status.DENIED);
    Bukkit.getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return false;
    }

    this.coHosts.remove(coHost);
    return true;
  }

  @Override
  public boolean coHost(final UUID uniqueId) {
    return this.coHosts.contains(uniqueId);
  }

  @Override
  public boolean isHost(final UUID uniqueId) {
    return this.host != null && this.host.equals(uniqueId);
  }

  @Override
  public boolean hasHostAccess(final UUID uniqueId) {
    return this.isHost(uniqueId) || this.coHost(uniqueId);
  }
}
