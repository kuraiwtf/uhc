package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import java.util.List;
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

  @Override
  public List<String> provideLore() {
    return Lists.newArrayList("Les objets deviennent incassables.");
  }

  @EventHandler
  public void onItemDamage(final PlayerItemDamageEvent event) {
    event.setCancelled(true);
  }
}
