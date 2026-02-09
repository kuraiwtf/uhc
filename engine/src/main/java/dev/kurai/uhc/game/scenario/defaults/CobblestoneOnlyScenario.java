package dev.kurai.uhc.game.scenario.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CobblestoneOnlyScenario extends AbstractScenario implements Listener {

  public CobblestoneOnlyScenario(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super("cobblestone_only", "Cobble Only", ultraHardcore);
  }

  @EventHandler
  public void onBlockBreak(final @NotNull BlockBreakEvent event) {
    this.apply(event.getBlock());
  }

  @EventHandler
  public void onBlockExplode(final @NotNull BlockExplodeEvent event) {
    this.apply(event.getBlock());
  }

  private void apply(final @NotNull Block block) {
    if (block.getType() != Material.STONE) {
      return;
    }

    block.setType(Material.AIR);
    block
        .getWorld()
        .dropItem(
            block.getLocation().clone().add(0.5, 0.5, 0.5), new ItemStack(Material.COBBLESTONE));
  }

  @Override
  public @NotNull ItemStack provideIcon() {
    return new ItemStack(Material.COBBLESTONE);
  }
}
