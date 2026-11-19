package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public final class TimberScenario extends AbstractScenario implements Listener {

    private static final int MAX_LOGS = 300;

    public TimberScenario(final UltraHardcoreAPI ultraHardcore) {
        super("timber", "Timber", ultraHardcore, ScenarioCategory.MINING);
    }

    private static void giveOrDrop(final Player player, final Location location, final ItemStack stack) {
        final var leftover = player.getInventory().addItem(stack);
        if (!leftover.isEmpty()) {
            location.getWorld().dropItemNaturally(location, stack);
        }
    }

    @Override
    public List<String> provideLore() {
        return Lists.newArrayList("Les arbres tombent automatiquement.");
    }

    @Override
    public ItemStack provideIcon() {
        return new ItemStack(Material.LOG);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final var block = event.getBlock();
        if (this.isNotLog(block.getType())) {
            return;
        }

        final var player = event.getPlayer();
        final var logs = this.check(block);

        event.setCancelled(true);
        giveOrDrop(
                player,
                block.getLocation().clone().add(0.5, 0.5, 0.5),
                new ItemStack(block.getType(), 1, block.getData()));
        block.getWorld().playSound(block.getLocation(), Sound.DIG_WOOD, 1f, 1f);
        block.setType(Material.AIR);

        new TimberTask(player, logs).runTaskTimer(this.ultraHardcore.plugin(), 0, 4L);
    }

    private List<Block> check(final Block block) {
        final var checkedBlocks = Sets.<Block>newHashSet();
        final var logBlocks = Lists.<Block>newArrayList();
        final var toCheck = Queues.<Block>newArrayDeque();

        checkedBlocks.add(block);
        toCheck.add(block);

        while (!toCheck.isEmpty() && logBlocks.size() < MAX_LOGS) {
            final var current = toCheck.poll();

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        final var blockAt =
                                current.getLocation().clone().add(x, y, z).getBlock();
                        if (!checkedBlocks.add(blockAt)) {
                            continue;
                        }

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
            if (!this.player.isOnline()) {
                this.cancel();
                return;
            }

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
                giveOrDrop(this.player, log.getLocation().clone().add(0.5, 0.5, 0.5), stack);

                world.playSound(log.getLocation(), Sound.DIG_WOOD, 1f, 1f);
                log.setType(Material.AIR);
            }
        }
    }
}
