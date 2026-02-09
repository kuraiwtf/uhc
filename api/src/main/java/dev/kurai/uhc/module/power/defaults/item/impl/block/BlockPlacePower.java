package dev.kurai.uhc.module.power.defaults.item.impl.block;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class BlockPlacePower extends AbstractItemPower {

  public BlockPlacePower(final @NotNull UUID owner, final @NotNull UltraHardcoreAPI ultraHardcore) {
    super(owner, ultraHardcore);
  }
}
