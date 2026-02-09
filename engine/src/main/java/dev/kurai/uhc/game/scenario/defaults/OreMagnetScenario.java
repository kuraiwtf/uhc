package dev.kurai.uhc.game.scenario.defaults;

import static org.bukkit.Sound.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.scenario.CutCleanDropEvent;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class OreMagnetScenario extends AbstractScenario implements Listener {

  public OreMagnetScenario(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super("ore_magnet", "Ore Magnet", ultraHardcore);
  }

  @Override
  public @NotNull ItemStack provideIcon() {
    return new ItemStack(Material.ENDER_CHEST);
  }

  @EventHandler
  public void onCutCleanDrop(final CutCleanDropEvent event) {
    final var player = event.getPlayer();
    player.giveExp(event.getExperience());
    player.playSound(
        player.getLocation(), ORB_PICKUP, 0.5f, ThreadLocalRandom.current().nextFloat(0.6f, 1.2f));

    if (player.getInventory().addItem(event.getItem()).isEmpty()) {
      event.setCancelled(true);
    }
  }
}
