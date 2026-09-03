package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public final class NoFallScenario extends AbstractScenario implements Listener {

  public NoFallScenario(final UltraHardcoreAPI ultraHardcore) {
    super("no_fall", "No Fall", ultraHardcore, ScenarioCategory.GAMEPLAY);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.FEATHER);
  }

  @Override
  public List<String> provideLore() {
    return Lists.newArrayList("Les dégâts de chûte sont annulés.");
  }

  @EventHandler
  public void onDamage(final EntityDamageEvent event) {
    if (event.getEntity().getType() == EntityType.PLAYER
        && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
      event.setCancelled(true);
    }
  }
}
