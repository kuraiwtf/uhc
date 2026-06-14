package dev.kurai.uhc.game.scenario.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import dev.kurai.uhc.game.scenario.configuration.defaults.IntegerScenarioConfiguration;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public final class BetaZombieScenario extends AbstractScenario implements Listener {

  private static final ScenarioConfiguration<Integer> RATE_CONFIGURATION =
      new IntegerScenarioConfiguration("rate", 100);

  public BetaZombieScenario(final UltraHardcoreAPI ultraHardcore) {
    super("beta_zombie", "Beta Zombie", ultraHardcore);
    this.registerConfiguration(RATE_CONFIGURATION);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.FEATHER);
  }

  @EventHandler
  public void onEntityDeath(final EntityDeathEvent event) {
    if (event.getEntity().getType() != EntityType.ZOMBIE
        || ThreadLocalRandom.current().nextInt(100) > RATE_CONFIGURATION.getValue()) {
      return;
    }

    event.getDrops().add(new ItemStack(Material.FEATHER));
  }
}
