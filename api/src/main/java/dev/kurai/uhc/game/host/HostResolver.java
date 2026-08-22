package dev.kurai.uhc.game.host;

import java.util.UUID;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface HostResolver {

  @Nullable UUID resolveHost();
}
