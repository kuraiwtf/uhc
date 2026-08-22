package dev.kurai.uhc.module.power.defaults.item.impl.player.impl;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.impl.player.PlayerTargetItemPower;
import java.util.UUID;

public abstract class LeftClickPlayerTargetItemPower extends PlayerTargetItemPower {
  public LeftClickPlayerTargetItemPower(
      final String identifier,
      final String name,
      final UUID owner,
      final UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
  }
}
