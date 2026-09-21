package dev.kurai.uhc.timer.builtin;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.death.GamePreDeathEvent;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Identifier(PvPTimer.IDENTIFIER)
@Name("PvP")
@Duration(min = 15 * 60, defaultValue = 20 * 60, max = 60 * 60)
public final class PvPTimer extends AbstractTimer implements Listener {

  static final String IDENTIFIER = "pvp";

  private final UltraHardcoreAPI ultraHardcore;

  public PvPTimer(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void onStart() {
    this.ultraHardcore.eventService().registerListener(this);
  }

  @Override
  public void onEnd() {
    this.ultraHardcore.eventService().unregisterListener(this);
    this.ultraHardcore.worldService().getWorld().setPVP(true);
  }

  @EventHandler
  public void onPreDeath(final GamePreDeathEvent event) {
    final Player player = event.getVictim().getPlayer();
    if (player != null) {
      player.sendMessage(CC.prefix("Vous avez été automatiquement§d ressuscité§f."));
      event.setRespawnLocation(player.getLocation());
    }

    event.setCancelled(true);
  }
}
