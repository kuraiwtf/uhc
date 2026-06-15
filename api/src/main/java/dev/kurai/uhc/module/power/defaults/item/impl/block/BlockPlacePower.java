package dev.kurai.uhc.module.power.defaults.item.impl.block;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class BlockPlacePower extends AbstractItemPower {

  public BlockPlacePower(
      final  String identifier,
      final  String name,
      final  UUID owner,
      final  UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
  }
}
