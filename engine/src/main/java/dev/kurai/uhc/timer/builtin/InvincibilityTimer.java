package dev.kurai.uhc.timer.builtin;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@Identifier(InvincibilityTimer.IDENTIFIER)
@Name("Invincibilité")
@Duration(min = 30, defaultValue = 60, max = 120)
public final class InvincibilityTimer extends AbstractTimer implements Listener {

  static final String IDENTIFIER = "invincibility";

  private static final Key ACTIONBAR_KEY = UltraHardcoreKey.key(IDENTIFIER);

  private final UltraHardcoreAPI ultraHardcore;

  public InvincibilityTimer(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void onStart() {
    this.ultraHardcore.eventService().registerListener(this);
  }

  @Override
  public void onSecond() {
    for (final var profile : this.ultraHardcore.profileService().getPlayingProfiles()) {
      profile
          .getActionbar()
          .registerActionbarEntry(
              ACTIONBAR_KEY,
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
    this.ultraHardcore.eventService().unregisterListener(this);

    for (final var profile : this.ultraHardcore.profileService().getPlayingProfiles()) {
      profile.getActionbar().unregisterActionbarEntry(ACTIONBAR_KEY);
      profile.sendPrefixedMessage("Vous êtes désormais&c vulnérable&r.");
      profile
          .findPlayer()
          .ifPresent(player -> player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f));
    }
  }

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    final Entity entity = event.getEntity();
    if (entity.getType() != EntityType.PLAYER) {
      return;
    }

    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(entity.getUniqueId());
    if (!profile.hasComponent(SpectatorComponent.class)) {
      event.setCancelled(true);
    }
  }
}
