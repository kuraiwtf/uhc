package dev.kurai.uhc.module.power.defaults.item.impl.player;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerTargetItemPower extends AbstractItemPower {

  protected Player target;

  public PlayerTargetItemPower(
      final  String identifier,
      final  String name,
      final  UUID owner,
      final  UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
    this.target = null;
  }

  public abstract int getRange();

  public final @Nullable Player getTarget() {
    return this.target;
  }

  public final void setTarget(final @Nullable Player target) {
    this.target = target;
  }
}
