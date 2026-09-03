package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import dev.kurai.uhc.game.scenario.configuration.defaults.IntegerScenarioConfiguration;
import java.util.List;
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
    super("beta_zombie", "Beta Zombie", ultraHardcore, ScenarioCategory.GAMEPLAY);
    this.registerConfiguration(RATE_CONFIGURATION);
  }

  @Override
  public List<String> provideLore() {
    return Lists.newArrayList(
        "Les Zombies ont une chance de", "faire tomber des plumes à leur mort.");
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
