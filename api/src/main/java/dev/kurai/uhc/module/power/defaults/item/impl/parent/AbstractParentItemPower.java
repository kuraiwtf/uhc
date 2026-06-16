package dev.kurai.uhc.module.power.defaults.item.impl.parent;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.LeftClickItemPower;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public abstract class AbstractParentItemPower extends LeftClickItemPower {

  private static final long SWITCH_DELAY = 200L;

  private final List<AbstractItemPower> children;
  private AbstractItemPower currentPower;
  private long lastSwitch;

  public AbstractParentItemPower(
      final String identifier,
      final String name,
      final UUID owner,
      final UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
    this.children = Lists.newArrayList();
  }

  @Override
  public final boolean onUse(final Player player) {
    if (this.getChildren().isEmpty()
        || System.currentTimeMillis() - this.lastSwitch < SWITCH_DELAY) {
      return false;
    }

    this.currentPower =
        this.children.get((this.children.indexOf(this.currentPower) + 1) % this.children.size());
    this.lastSwitch = System.currentTimeMillis();

    player.setItemInHand(this.currentPower.getIcon(player));
    player.updateInventory();

    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1F, 1F);
    return true;
  }

  @Override
  public ItemStack getIcon(final Player player) {
    if (this.currentPower == null) {
      return super.getIcon(player);
    }

    return this.currentPower.getIcon(player);
  }

  public final void registerChild(final AbstractItemPower power) {
    this.children.add(power);
  }

  public final void registerChildren(final AbstractItemPower... powers) {
    for (final var power : powers) {
      this.registerChild(power);
    }
  }

  public final Collection<AbstractItemPower> getChildren() {
    return this.children;
  }

  public final @Nullable AbstractItemPower getCurrentPower() {
    return this.currentPower;
  }

  public final void setCurrentPower(final @Nullable AbstractItemPower currentPower) {
    this.currentPower = currentPower;
  }
}
