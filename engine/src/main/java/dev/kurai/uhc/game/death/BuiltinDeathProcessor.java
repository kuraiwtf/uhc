package dev.kurai.uhc.game.death;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.profile.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

final class BuiltinDeathProcessor implements DeathProcessor {

  private final UltraHardcoreAPI ultraHardcore;
  private final DeathService deathService;

  BuiltinDeathProcessor(final UltraHardcoreAPI ultraHardcore, final DeathService deathService) {
    this.ultraHardcore = ultraHardcore;
    this.deathService = deathService;
  }

  @Override
  public void processDeath(final DeathContext context) {
    final PlayerDeathEvent event = context.event();

    final Player player = event.getEntity();
    final Player killer = player.getKiller();
    if (killer != null) {
      killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
      killer.giveExpLevels(3);
    }

    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    this.deathService.eliminate(
        profile,
        killer == null ? null : this.ultraHardcore.profileService().getOrCreateProfile(killer),
        false);
  }
}
