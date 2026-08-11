package dev.kurai.uhc.game.scenario.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public final class UnbreakableScenario extends AbstractScenario implements Listener {

  public UnbreakableScenario(final UltraHardcoreAPI ultraHardcore) {
    super("unbreakable", "Unbreakable", ultraHardcore, ScenarioCategory.GAMEPLAY);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.ANVIL);
  }

  @EventHandler
  public void onEntityDeath(final PlayerItemDamageEvent event) {
    event.setCancelled(true);
  }
}
