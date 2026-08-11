package dev.kurai.uhc.game.death;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.minimessage.tag.Tag.inserting;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;

import dev.kurai.uhc.profile.Profile;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class BuiltinDeathAnnounce implements DeathAnnounce {

  @Override
  public Component provideDeathMessage(
      final Profile profile, final @Nullable Profile killer, final boolean offline) {
    if (killer == null) {
      return miniMessage()
          .deserialize(
              "<yellow><b>UHC</b></yellow> <gray><b>|</b></gray> <red><victim></red> est mort.",
              resolver("victim", inserting(text(profile.getName()))));
    }

    return miniMessage()
        .deserialize(
            "<yellow><b>UHC</b></yellow> <gray><b>|</b></gray> <red><victim></red> est mort de <red><killer></red>.",
            resolver(
                resolver("victim", inserting(text(profile.getName()))),
                resolver("killer", inserting(text(killer.getName())))));
  }
}
