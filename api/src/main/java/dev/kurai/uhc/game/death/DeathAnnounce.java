package dev.kurai.uhc.game.death;

import dev.kurai.uhc.profile.Profile;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface DeathAnnounce {

  Component provideDeathMessage(
      final Profile profile, final @Nullable Profile killer, final boolean offline);

  default void onDeath(
      final Profile profile, final @Nullable Profile killer, final boolean offline) {}
}
