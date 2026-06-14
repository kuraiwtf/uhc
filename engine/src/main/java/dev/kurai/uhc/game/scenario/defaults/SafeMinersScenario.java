package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public final class SafeMinersScenario extends AbstractScenario implements Listener {

  private static final Collection<DamageCause> IMMUNE =
      Lists.newArrayList(
          DamageCause.FALL,
          DamageCause.FALLING_BLOCK,
          DamageCause.FIRE_TICK,
          DamageCause.FIRE,
          DamageCause.LAVA);

  public SafeMinersScenario(final UltraHardcoreAPI ultraHardcore) {
    super("safe_miners", "Safe Miners", ultraHardcore);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.LAVA_BUCKET);
  }

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    if (event.getEntity().getType() != EntityType.PLAYER
        || !IMMUNE.contains(event.getCause())
        || event.getEntity().getLocation().getY() > 40) {
      return;
    }

    event.setCancelled(true);
  }
}
