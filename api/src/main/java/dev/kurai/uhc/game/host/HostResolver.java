package dev.kurai.uhc.game.host;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

@FunctionalInterface
public interface HostResolver {

    @Nullable
    UUID resolveHost();
}
