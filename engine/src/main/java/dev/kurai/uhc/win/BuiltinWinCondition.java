package dev.kurai.uhc.win;

import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.team.Team;
import dev.kurai.uhc.team.TeamService;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class BuiltinWinCondition implements WinCondition {

    private final ProfileService profileService;
    private final TeamService teamService;

    public BuiltinWinCondition(final ProfileService profileService, final TeamService teamService) {
        this.profileService = profileService;
        this.teamService = teamService;
    }

    @Override
    public @Nullable @Unmodifiable Collection<Profile> validateWin() {
        final var playingProfiles =
                this.profileService.getProfiles(profile -> profile.getState() instanceof PlayingProfileState);
        if (playingProfiles.isEmpty()) {
            return Collections.emptyList();
        }

        final var groups = playingProfiles.stream()
                .collect(Collectors.groupingBy(profile -> this.teamService
                        .teamOf(profile.getId())
                        .<Object>map(team -> team)
                        .orElse(profile.getId())));
        if (groups.size() > 1) {
            return null;
        }

        final var survivor = groups.keySet().iterator().next();
        if (survivor instanceof final Team team) {
            return this.profileService.getProfiles(profile -> team.contains(profile.getId()));
        }

        return playingProfiles;
    }
}
