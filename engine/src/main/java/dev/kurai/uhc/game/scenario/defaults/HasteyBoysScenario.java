package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public final class HasteyBoysScenario extends AbstractScenario implements Listener {

  private static final Collection<Material> TOOLS = Lists.newArrayList();

  static {
    for (final var material : Material.values()) {
      if (material.name().contains("PICKAXE")
          || material.name().contains("AXE")
          || material.name().contains("SPADE")
          || material.name().contains("HOE")) {
        TOOLS.add(material);
      }
    }

    TOOLS.add(Material.SHEARS);
  }

  public HasteyBoysScenario(final UltraHardcoreAPI ultraHardcore) {
    super("hastey_boys", "Hastey Boys", ultraHardcore, ScenarioCategory.MINING);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.IRON_PICKAXE);
  }

  @EventHandler
  public void onPrepareItemCraft(final PrepareItemCraftEvent event) {
    final var result = event.getRecipe().getResult();
    if (!TOOLS.contains(result.getType())) {
      return;
    }

    result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);
    result.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
    event.getInventory().setResult(result);
  }
}
