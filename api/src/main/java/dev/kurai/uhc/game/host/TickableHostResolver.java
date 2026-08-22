package dev.kurai.uhc.game.host;

import java.util.UUID;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface TickableHostResolver extends HostResolver {

  @Override
  @Nullable UUID resolveHost();
}
