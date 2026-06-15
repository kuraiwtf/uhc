package dev.kurai.uhc.timer.builtin;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

@Identifier(PvPTimer.IDENTIFIER)
@Name("PvP")
@Duration(min = 15 * 60, defaultValue = 20 * 60, max = 60 * 60)
public final class PvPTimer extends AbstractTimer {

  static final String IDENTIFIER = "pvp";

  private final BukkitAudiences bukkitAudiences;

  public PvPTimer(final BukkitAudiences bukkitAudiences) {
    this.bukkitAudiences = bukkitAudiences;
  }

  @Override
  public void onEnd() {
    this.bukkitAudiences
        .all()
        .sendMessage(text("Le PvP est désormais ").append(text("actif", GREEN)).append(text('.')));

    Bukkit.getOnlinePlayers()
        .forEach(
            player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f));
  }
}
