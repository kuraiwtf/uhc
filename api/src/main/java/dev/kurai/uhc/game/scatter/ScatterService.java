package dev.kurai.uhc.game.scatter;

import org.bukkit.entity.Player;

public interface ScatterService {

  ScatterPositionProvider getPositionProvider();

  void setPositionProvider(final ScatterPositionProvider positionProvider);

  void handleScatter();

  void handlePlayerLateScatter(final Player player);
}
