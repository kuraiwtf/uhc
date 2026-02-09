package dev.kurai.uhc.game.drop.defaults;

import static org.bukkit.Material.*;

import dev.kurai.uhc.game.drop.AbstractDropRateModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class FlintDropRateModifier extends AbstractDropRateModifier implements Listener {

  public FlintDropRateModifier() {
    super("flint", "Silex");
  }

  @Override
  public ItemStack getIcon() {
    return new ItemStack(FLINT);
  }

  @EventHandler(ignoreCancelled = true)
  public void onBlockBreak(final BlockBreakEvent event) {
    final var block = event.getBlock();
    if (block.getType() != GRAVEL || this.random.nextInt(100) > this.dropRate) {
      return;
    }

    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(FLINT));
  }
}
