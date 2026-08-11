package dev.kurai.uhc.game.scenario.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import dev.kurai.uhc.game.scenario.configuration.ScenarioConfiguration;
import dev.kurai.uhc.game.scenario.configuration.defaults.IntegerScenarioConfiguration;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public final class BatsScenario extends AbstractScenario implements Listener {

  private static final ScenarioConfiguration<Integer> RATE_CONFIGURATION =
      new IntegerScenarioConfiguration("rate", 99);

  public BatsScenario(final UltraHardcoreAPI ultraHardcore) {
    super("bats", "Bats", ultraHardcore, ScenarioCategory.GAMEPLAY);
    this.registerConfiguration(RATE_CONFIGURATION);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.MONSTER_EGG, 1, (short) 65);
  }

  @EventHandler
  public void onEntityDeath(final EntityDeathEvent event) {
    final LivingEntity entity = event.getEntity();
    if (entity.getType() == EntityType.BAT) {
      if (ThreadLocalRandom.current().nextInt(100) > RATE_CONFIGURATION.getValue()) {
        final Player killer = entity.getKiller();
        if (killer != null) {
          killer.setHealth(0);
        }
        return;
      }

      event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
    }
  }
}
