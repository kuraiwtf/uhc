package dev.kurai.uhc.whitelist;

import java.util.Collection;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface WhitelistService {

  Collection<WhitelistMeta> getWhitelistedPlayers();

  void whitelist(final UUID executor, final UUID id, final String reason);

  void unwhitelist(final UUID id);

  boolean isWhitelisted(final UUID id);
}
