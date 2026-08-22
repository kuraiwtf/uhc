package dev.kurai.uhc.game.scenario.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.ItemStack;

public final class CobblestoneOnlyScenario extends AbstractScenario implements Listener {

  public CobblestoneOnlyScenario(final UltraHardcoreAPI ultraHardcore) {
    super("cobblestone_only", "Cobble Only", ultraHardcore, ScenarioCategory.MINING);
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    this.apply(event.getBlock());
  }

  @EventHandler
  public void onBlockExplode(final BlockExplodeEvent event) {
    this.apply(event.getBlock());
  }

  private void apply(final Block block) {
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
  public ItemStack provideIcon() {
    return new ItemStack(Material.COBBLESTONE);
  }
}
