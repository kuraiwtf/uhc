package dev.kurai.uhc.game.death;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jspecify.annotations.Nullable;

public final class BuiltinDeathAnnounce implements DeathAnnounce {

  @Override
  public Component provideDeathMessage(
      final Profile profile, final @Nullable Profile killer, final boolean offline) {
    final String killerName =
        offline ? "déconnexion" : (killer == null ? "Rien" : killer.getName());

    return text()
        .append(CC.line(GOLD, YELLOW))
        .appendNewline()
        .appendNewline()
        .appendSpace()
        .append(text('»', DARK_GRAY))
        .appendSpace()
        .append(text(profile.getName()).decorate(TextDecoration.BOLD))
        .appendSpace()
        .append(text("est mort de "))
        .append(text(killerName, RED))
        .append(text('.'))
        .appendNewline()
        .appendNewline()
        .append(CC.line(GOLD, YELLOW))
        .build();
  }
}
