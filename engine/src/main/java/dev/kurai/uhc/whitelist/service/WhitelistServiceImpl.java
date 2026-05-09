package dev.kurai.uhc.whitelist.service;

import com.google.common.collect.Maps;
import dev.kurai.uhc.whitelist.meta.WhitelistMeta;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WhitelistServiceImpl implements WhitelistService {

  private final Map<UUID, WhitelistMeta> whitelistedPlayers;

  public WhitelistServiceImpl() {
    this.whitelistedPlayers = Maps.newHashMap();
  }

  @Override
  public Collection<WhitelistMeta> getWhitelistedPlayers() {
    return this.whitelistedPlayers.values();
  }

  @Override
  public void whitelist(final UUID executor, final UUID id, final String reason) {
    this.whitelistedPlayers.put(id, new WhitelistMeta(id, executor, reason, LocalDateTime.now()));
  }

  @Override
  public void unwhitelist(final UUID id) {
    this.whitelistedPlayers.remove(id);
  }

  @Override
  public boolean isWhitelisted(final UUID id) {
    return this.whitelistedPlayers.containsKey(id);
  }
}
