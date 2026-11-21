package dev.kurai.uhc.whitelist;

import com.google.common.collect.Maps;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@NullMarked
public final class WhitelistServiceImpl implements WhitelistService {

    private final Map<UUID, WhitelistMeta> whitelistedPlayers = Maps.newConcurrentMap();

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
    public boolean whitelisted(final UUID id) {
        return this.whitelistedPlayers.containsKey(id);
    }
}
