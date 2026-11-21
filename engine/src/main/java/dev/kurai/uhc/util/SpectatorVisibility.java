package dev.kurai.uhc.util;

import dev.kurai.uhc.UltraHardcoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SpectatorVisibility {

    private SpectatorVisibility() {}

    public static void refresh(final UltraHardcoreAPI ultraHardcore) {
        final var profileService = ultraHardcore.profileService();
        final var online = Bukkit.getOnlinePlayers();

        for (final Player viewer : online) {
            final boolean viewerIsSpectator =
                    profileService.getOrCreateProfile(viewer).spectating();

            for (final Player target : online) {
                if (viewer.equals(target)) {
                    continue;
                }

                final boolean targetIsSpectator =
                        profileService.getOrCreateProfile(target).spectating();

                if (targetIsSpectator && !viewerIsSpectator) {
                    viewer.hidePlayer(target);
                } else {
                    viewer.showPlayer(target);
                }
            }
        }
    }
}
