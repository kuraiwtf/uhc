package dev.kurai.uhc.game.scatter;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ScatterService {

  @NotNull
  ScatterPositionProvider getPositionProvider();

  void setPositionProvider(final @NotNull ScatterPositionProvider positionProvider);

  void handleScatter();

  void handlePlayerLateScatter(final @NotNull Player player);
}
