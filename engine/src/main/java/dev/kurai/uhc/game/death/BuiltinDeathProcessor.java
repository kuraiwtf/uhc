package dev.kurai.uhc.game.death;

import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.profile.Profile;
import net.kyori.adventure.sound.Sound;
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
    final GameService gameService = this.ultraHardcore.gameService();
    gameService.playSound(
        Sound.sound()
            .source(Sound.Source.HOSTILE)
            .type(key("mob.wither.death"))
            .volume(1f)
            .pitch(1f)
            .build());

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
