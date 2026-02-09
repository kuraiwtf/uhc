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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class SafeMinersScenario extends AbstractScenario implements Listener {

  private static final Collection<EntityDamageEvent.@NotNull DamageCause> IMMUNE =
      Lists.newArrayList(
          EntityDamageEvent.DamageCause.FALL,
          EntityDamageEvent.DamageCause.FALLING_BLOCK,
          EntityDamageEvent.DamageCause.FIRE_TICK,
          EntityDamageEvent.DamageCause.FIRE,
          EntityDamageEvent.DamageCause.LAVA);

  public SafeMinersScenario(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super("safe_miners", "Safe Miners", ultraHardcore);
  }

  @Override
  public @NotNull ItemStack provideIcon() {
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
