package dev.kurai.uhc.game.scatter;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public final class ScatterTask extends BukkitRunnable {

    private final UltraHardcoreAPI ultraHardcore;

    /** Each entry is a group scattered at the same position: a whole team, or a lone player. */
    private final List<List<UUID>> groups;

    private final List<Location> positions;

    public ScatterTask(final UltraHardcoreAPI ultraHardcore) {
        this.ultraHardcore = ultraHardcore;

        final var teamService = ultraHardcore.teamService();
        final var remaining = Lists.newArrayList(
                ultraHardcore
                        .profileService()
                        .getProfiles(profile -> !profile.hasComponent(SpectatorComponent.class))
                        .stream()
                        .map(Profile::getId)
                        .toList());

        this.groups = Lists.newArrayList();
        while (!remaining.isEmpty()) {
            final var id = remaining.removeFirst();
            final var team = teamService.teamOf(id).orElse(null);
            if (team == null) {
                this.groups.add(List.of(id));
                continue;
            }

            final var group = Lists.newArrayList(id);
            for (final var member : team.members()) {
                if (remaining.remove(member)) {
                    group.add(member);
                }
            }
            this.groups.add(group);
        }

        this.positions = Lists.newArrayList(ultraHardcore
                .gameService()
                .scatterService()
                .getPositionProvider()
                .provideLocations(BorderConfiguration.INITIAL_SIZE_OPTION.getValue() - 50, this.groups.size()));
    }

    @Override
    public void run() {
        if (this.groups.isEmpty()) {
            this.ultraHardcore.gameService().startService().handleFinalStart();
            this.cancel();
            return;
        }

        final var group = this.groups.removeFirst();
        final var location = this.positions.removeFirst();
        location.getChunk().load(true);

        for (final var found : group) {
            final var player = Bukkit.getPlayer(found);
            if (player != null) {
                player.teleport(location);
            }
        }
    }
}
