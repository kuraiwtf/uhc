package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.player.PlayerDamageByPlayerEvent;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import java.util.List;
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

  @Override
  public List<String> provideLore() {
    return Lists.newArrayList("Le délai des coups est supprimé.");
  }

  @EventHandler
  public void onPlayerDamage(final PlayerDamageByPlayerEvent event) {
    final Player player = event.victim().getPlayer();
    if (player != null) {
      player.setNoDamageTicks(0);
    }
  }
}
