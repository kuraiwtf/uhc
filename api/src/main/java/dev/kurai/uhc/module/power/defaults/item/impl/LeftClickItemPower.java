package dev.kurai.uhc.module.power.defaults.item.impl;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class LeftClickItemPower extends AbstractItemPower {

  public LeftClickItemPower(
      final @NotNull String identifier,
      final @NotNull String name,
      final @NotNull UUID owner,
      final @NotNull UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
  }
}
