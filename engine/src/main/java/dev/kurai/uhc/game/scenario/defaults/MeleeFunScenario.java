package dev.kurai.uhc.game.scenario.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.player.PlayerDamageByPlayerEvent;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public final class MeleeFunScenario extends AbstractScenario implements Listener {

  public MeleeFunScenario(final UltraHardcoreAPI ultraHardcore) {
    super("melee_fun", "Melee Fun", ultraHardcore, ScenarioCategory.COMBAT);
  }

  @Override
  public ItemStack provideIcon() {
    return new ItemStack(Material.IRON_SWORD);
  }

  @EventHandler
  public void onEntityDeath(final PlayerDamageByPlayerEvent event) {
    final Player player = event.victim().getPlayer();
    if (player == null) {
      return;
    }

    player.setNoDamageTicks(0);
  }
}
