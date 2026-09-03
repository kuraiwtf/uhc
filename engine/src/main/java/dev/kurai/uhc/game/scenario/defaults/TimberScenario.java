package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public final class TimberScenario extends AbstractScenario implements Listener {

  public TimberScenario(final UltraHardcoreAPI ultraHardcore) {
    super("timber", "Timber", ultraHardcore, ScenarioCategory.MINING);
  }

  @Override
  public List<String> provideLore() {
    return Lists.newArrayList("Les arbres tombent automatiquement.");
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.LOG);
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    final var block = event.getBlock();
    if (this.isNotLog(block.getType())) {
      return;
    }

    event.getBlock().setType(Material.AIR);
    new TimberTask(event.getPlayer(), this.check(block))
        .runTaskTimer(this.ultraHardcore.plugin(), 0, 4L);
  }

  private List<Block> check(final Block block) {
    final var checkedBlocks = Lists.<Block>newArrayList();
    final var logBlocks = Lists.<Block>newArrayList();
    final var toCheck = Queues.<Block>newConcurrentLinkedQueue();

    checkedBlocks.add(block);
    toCheck.add(block);

    while (!toCheck.isEmpty()) {
      final var current = toCheck.poll();

      for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
          for (int z = -1; z <= 1; z++) {
            final var blockAt = current.getLocation().clone().add(x, y, z).getBlock();
            if (checkedBlocks.contains(blockAt)) {
              continue;
            }

            checkedBlocks.add(blockAt);

            if (this.isNotLog(blockAt.getType())) {
              continue;
            }

            toCheck.add(blockAt);
            logBlocks.add(blockAt);
          }
        }
      }
    }

    return logBlocks;
  }

  private boolean isNotLog(final Material material) {
    return !material.name().contains("LOG");
  }

  private static final class TimberTask extends BukkitRunnable {

    private final Player player;
    private final List<Block> blocks;

    public TimberTask(final Player player, final List<Block> blocks) {
      this.player = player;
      this.blocks = blocks;
    }

    @Override
    public void run() {
      for (int i = 0; i < 4; i++) {
        if (this.blocks.isEmpty()) {
          this.cancel();
          return;
        }

        final var log = this.blocks.removeFirst();
        if (log.getType() == Material.AIR) {
          continue;
        }

        final var world = log.getWorld();
        final var stack = new ItemStack(log.getType(), 1, log.getData());
        final var leftover = this.player.getInventory().addItem(stack);
        if (!leftover.isEmpty()) {
          world.dropItemNaturally(log.getLocation().clone().add(0.5, 0.5, 0.5), stack);
        }

        world.playSound(log.getLocation(), Sound.DIG_WOOD, 1f, 1f);
        log.setType(Material.AIR);
      }
    }
  }
}
