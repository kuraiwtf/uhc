package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

public final class NoFoodScenario extends AbstractScenario implements Listener {

  public NoFoodScenario(final UltraHardcoreAPI ultraHardcore) {
    super("no_food", "No Food", ultraHardcore, ScenarioCategory.GAMEPLAY);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.GOLDEN_CARROT);
  }

  @Override
  public List<String> provideLore() {
    return Lists.newArrayList("La nourriture est supprimée.");
  }

  @EventHandler
  public void onFoodLevelChange(final FoodLevelChangeEvent event) {
    if (!(event.getEntity() instanceof final Player player)) {
      return;
    }

    event.setCancelled(true);

    player.setFoodLevel(20);
    player.setSaturation(20);
  }
}
