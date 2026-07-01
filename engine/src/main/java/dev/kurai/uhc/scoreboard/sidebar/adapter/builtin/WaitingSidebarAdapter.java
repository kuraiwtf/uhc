package dev.kurai.uhc.scoreboard.sidebar.adapter.builtin;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.scoreboard.sidebar.SidebarAdapter;
import dev.kurai.uhc.scoreboard.sidebar.SidebarTitleAdapter;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public final class WaitingSidebarAdapter implements SidebarAdapter, SidebarTitleAdapter {

  private final UltraHardcoreAPI ultraHardcore;

  public WaitingSidebarAdapter(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public Component provideTitle(final Player player) {
    final AbstractModule module = this.ultraHardcore.moduleService().getCurrentModule();
    return text()
        .append(text('-', DARK_GRAY, BOLD))
        .appendSpace()
        .append(text(module.getShortName(), GOLD, BOLD))
        .appendSpace()
        .append(text('-', DARK_GRAY, BOLD))
        .build();
  }

  @Override
  public List<Component> provideLines(final Player player) {
    final var lines = Lists.<Component>newArrayList();
    final var gameService = this.ultraHardcore.gameService();
    final var hostId = gameService.hostService().host();
    lines.add(empty());
    final ProfileService profileService = this.ultraHardcore.profileService();
    lines.add(
        text()
            .append(SEPARATOR)
            .append(text("Hôte: ", GRAY))
            .append(
                text(
                    hostId == null ? "Aucun" : profileService.getOrCreateProfile(hostId).getName(),
                    hostId == null ? RED : GOLD))
            .build());
    final var module = this.ultraHardcore.moduleService().getCurrentModule();
    lines.add(
        text()
            .append(SEPARATOR)
            .append(text("Jeu: ", GRAY))
            .append(text(module.getShortName(), GOLD))
            .build());
    lines.add(
        text()
            .append(SEPARATOR)
            .append(text("Joueurs: ", GRAY))
            .append(
                text(
                    profileService
                        .getProfiles(profile -> !profile.hasComponent(SpectatorComponent.class))
                        .size(),
                    GREEN))
            .append(text('/', DARK_GRAY))
            .append(text(gameService.slotService().slotProvider().slots(), GREEN))
            .build());
    if (module instanceof final TeamModule teamModule) {
      lines.add(
          text()
              .append(SEPARATOR)
              .append(text("Équipes: ", GRAY))
              .append(
                  text(
                      teamModule.teamSize() == 1
                          ? "FFA"
                          : "%dvs%d".formatted(teamModule.teamSize(), teamModule.teamSize()),
                      LIGHT_PURPLE))
              .build());
    }
    lines.add(empty());
    lines.add(text().append(text('@', DARK_AQUA)).append(text("kuraiwtf", AQUA)).build());
    return lines;
  }

  private TextColor provideTpsColor(final double tps) {
    return tps > 15 ? GREEN : tps > 10 ? YELLOW : tps > 5 ? GOLD : RED;
  }
}
