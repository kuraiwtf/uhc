package dev.kurai.uhc.scoreboard.sidebar.adapter.builtin;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.SpectatingProfileComponent;
import dev.kurai.uhc.scoreboard.sidebar.SidebarAdapter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class WaitingSidebarAdapter implements SidebarAdapter {

    private final UltraHardcoreAPI ultraHardcore;

    public WaitingSidebarAdapter(final UltraHardcoreAPI ultraHardcore) {
        this.ultraHardcore = ultraHardcore;
    }

    @Override
    public List<Component> provideLines(final Player player) {
        final var lines = Lists.<Component>newArrayList();
        final var gameService = this.ultraHardcore.gameService();
        final var hostId = gameService.hostService().host();
        lines.add(empty());
        final ProfileService profileService = this.ultraHardcore.profileService();
        lines.add(text().append(SEPARATOR)
                .append(text("Hôte: ", GRAY))
                .append(text(
                        hostId == null
                                ? "Aucun"
                                : profileService.getOrCreateProfile(hostId).getName(),
                        hostId == null ? RED : GOLD))
                .build());
        final var module = this.ultraHardcore.moduleService().getCurrentModule();
        lines.add(text().append(SEPARATOR)
                .append(text("Jeu: ", GRAY))
                .append(text(module.getShortName(), GOLD))
                .build());
        lines.add(text().append(SEPARATOR)
                .append(text("Joueurs: ", GRAY))
                .append(text(
                        profileService
                                .getProfiles(profile -> profile.findPlayer().isPresent()
                                        && !profile.hasComponent(SpectatingProfileComponent.class))
                                .size(),
                        GREEN))
                .append(text('/', DARK_GRAY))
                .append(text(gameService.slotService().slotProvider().slots(), GREEN))
                .build());
        if (module instanceof final TeamModule teamModule) {
            final int teamSize = teamModule.teamSize();
            lines.add(text().append(SEPARATOR)
                    .append(text("Équipes: ", GRAY))
                    .append(text(
                            teamSize == 1
                                    ? "FFA"
                                    : "%s To%d".formatted(teamModule.randomTeam() ? "Random" : "Chosen", teamSize),
                            LIGHT_PURPLE))
                    .build());
        }
        lines.add(empty());
        lines.add(this.ultraHardcore.moduleService().getCurrentModule().sidebarCredit());
        return lines;
    }
}
