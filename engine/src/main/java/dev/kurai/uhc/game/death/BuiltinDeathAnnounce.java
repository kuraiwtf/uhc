package dev.kurai.uhc.game.death;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.minimessage.tag.Tag.inserting;
import static net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.resolver;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.team.TeamService;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class BuiltinDeathAnnounce implements DeathAnnounce {

  private final TeamService teamService;

  public BuiltinDeathAnnounce(final TeamService teamService) {
    this.teamService = teamService;
  }

  @Override
  public Component provideDeathMessage(
      final Profile profile, final @Nullable Profile killer, final boolean offline) {
    if (killer == null) {
      return miniMessage()
          .deserialize(
              "<yellow><b>UHC</b></yellow> <gray><b>|</b></gray> <red><victim></red> est mort.",
              resolver("victim", inserting(this.withTeam(profile))));
    }

    return miniMessage()
        .deserialize(
            "<yellow><b>UHC</b></yellow> <gray><b>|</b></gray> <red><victim></red> est mort de <red><killer></red>.",
            resolver(
                resolver("victim", inserting(this.withTeam(profile))),
                resolver("killer", inserting(this.withTeam(killer)))));
  }

  private Component withTeam(final Profile profile) {
    final var name = text(profile.getName());
    return this.teamService
        .teamOf(profile.getId())
        .map(
            team ->
                name.append(
                    text(" (", DARK_GRAY)
                        .append(text(team.getName(), team.style().color().asTextColor()))
                        .append(text(")", DARK_GRAY))))
        .orElse(name);
  }
}
