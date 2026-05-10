package dev.kurai.uhc.module.power.defaults.item;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractItemPower extends AbstractPower {

  public AbstractItemPower(
      final @NotNull String identifier,
      final @NotNull String name,
      final @NotNull UUID owner,
      final @NotNull UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
  }

  public abstract @NotNull ItemStack provideIcon(final @NotNull Player player);

  public boolean shouldDistributePower(final Player player) {
    return true;
  }
}
