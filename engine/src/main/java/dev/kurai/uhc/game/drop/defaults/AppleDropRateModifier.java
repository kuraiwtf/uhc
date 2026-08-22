package dev.kurai.uhc.game.drop.defaults;

import static org.bukkit.Material.*;

import dev.kurai.uhc.game.drop.AbstractDropRateModifier;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AppleDropRateModifier extends AbstractDropRateModifier implements Listener {

  public AppleDropRateModifier() {
    super("apple", "Pomme");
  }

  @Override
  public ItemStack getIcon() {
    return new ItemStack(APPLE);
  }

  @EventHandler(ignoreCancelled = true)
  public void onBlockBreak(final BlockBreakEvent event) {
    final Block block = event.getBlock();
    if (!block.getType().name().contains("LEAVES") || this.random.nextInt(100) > this.dropRate) {
      return;
    }

    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(APPLE));
  }

  @EventHandler
  public void onDecay(final LeavesDecayEvent event) {
    if (this.random.nextInt(100) <= this.dropRate) {
      final Block block = event.getBlock();
      block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(APPLE));
    }
  }
}
