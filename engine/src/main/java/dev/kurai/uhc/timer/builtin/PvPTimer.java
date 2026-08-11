package dev.kurai.uhc.timer.builtin;

import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import dev.kurai.uhc.world.WorldService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

@Identifier(PvPTimer.IDENTIFIER)
@Name("PvP")
@Duration(min = 15 * 60, defaultValue = 20 * 60, max = 60 * 60)
public final class PvPTimer extends AbstractTimer {

  static final String IDENTIFIER = "pvp";

  private final WorldService worldService;

  public PvPTimer(final WorldService worldService) {
    this.worldService = worldService;
  }

  @Override
  public void onEnd() {
    this.worldService.getWorld().setPVP(true);

    Bukkit.getOnlinePlayers()
        .forEach(
            player -> {
              player.sendMessage(CC.prefix("Le PvP est désormais&a actif&r."));
              player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
            });
  }
}
