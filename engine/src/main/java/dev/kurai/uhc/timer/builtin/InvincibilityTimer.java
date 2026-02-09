package dev.kurai.uhc.timer.builtin;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

@Identifier(InvincibilityTimer.IDENTIFIER)
@Name("Invincibilité")
@Duration(min = 30, defaultValue = 60, max = 120)
public final class InvincibilityTimer extends AbstractTimer implements Listener {

  static final String IDENTIFIER = "invincibility";

  private final BukkitAudiences bukkitAudiences;
  private final UltraHardcoreAPI ultraHardcore;

  public InvincibilityTimer(
      final @NotNull BukkitAudiences bukkitAudiences,
      final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.bukkitAudiences = bukkitAudiences;
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void onStart() {
    this.ultraHardcore.getEventService().registerListener(this);
  }

  @Override
  public void onSecond() {
    for (final var profile : this.ultraHardcore.getProfileService().getProfiles()) {
      profile
          .getActionbar()
          .registerEntry(
              IDENTIFIER,
              text()
                  .append(text("Vous êtes "))
                  .append(text("invincible", GREEN))
                  .append(text(" durant "))
                  .append(text(this.getTimeLeft(), GREEN, TextDecoration.BOLD))
                  .append(text('s', GREEN))
                  .append(text('.'))
                  .build());
    }
  }

  @Override
  public void onEnd() {
    this.ultraHardcore.getEventService().unregisterListener(this);
    this.bukkitAudiences
        .all()
        .sendMessage(
            text("L'invincibilité est maintenant ", RED)
                .append(text("désactivée", DARK_RED))
                .append(text(".", RED)));

    for (final var profile : this.ultraHardcore.getProfileService().getProfiles()) {
      profile.getActionbar().removeEntry(IDENTIFIER);
    }

    Bukkit.getOnlinePlayers()
        .forEach(player -> player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f));
  }

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    if (event.getEntityType() != EntityType.PLAYER) {
      return;
    }

    event.setCancelled(true);
  }
}
