package dev.kurai.uhc.game.host;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

@FunctionalInterface
public interface TickableHostResolver extends HostResolver {

    @Override
    @Nullable
    UUID resolveHost();
}
