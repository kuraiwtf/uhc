package dev.kurai.uhc.module.power.defaults.item.impl.player.impl;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.impl.player.PlayerTargetItemPower;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class LeftClickPlayerTargetItemPower extends PlayerTargetItemPower {
  public LeftClickPlayerTargetItemPower(
      final @NotNull UUID owner, final @NotNull UltraHardcoreAPI ultraHardcore) {
    super(owner, ultraHardcore);
  }
}
