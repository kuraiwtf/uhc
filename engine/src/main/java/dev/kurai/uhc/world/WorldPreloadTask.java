package dev.kurai.uhc.world;

import dev.kurai.actionbar.Actionbar;
import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;

import static dev.kurai.uhc.util.TimeUtil.formatDuration;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public final class WorldPreloadTask extends BukkitRunnable {

    private static final int CHUNKS_PER_TICK = 10;

    private final ProfileService profileService;

    private final World world;
    private final int borderSize;

    private final int totalChunks;
    private final Instant start;
    private int loadedChunks;
    private int currentX;
    private int currentZ;

    public WorldPreloadTask(final ProfileService profileService, final World world, final int borderSize) {
        this.profileService = profileService;

        this.world = world;
        this.borderSize = borderSize;

        this.currentX = -borderSize;
        this.currentZ = -borderSize;

        final int chunksPerAxis = ((2 * borderSize) / 16) + 1;
        this.totalChunks = chunksPerAxis * chunksPerAxis;
        this.loadedChunks = 0;

        this.start = now();
    }

    @Override
    public void run() {
        for (int i = 0; i < CHUNKS_PER_TICK; i++) {
            this.world.getChunkAt(this.currentX >> 4, this.currentZ >> 4).load();
            this.loadedChunks++;
            final double progress = Math.min(100.0, (this.loadedChunks * 100.0) / this.totalChunks);

            final boolean report = i == 0;
            for (final Profile profile : this.profileService.getProfiles(
                    p -> report && p.findPlayer().isPresent())) {
                final Actionbar actionbar = profile.getActionbar();
                actionbar.registerActionbarEntry(
                        UltraHardcoreKey.key("preload"),
                        text().append(text("C/s: "))
                                .append(text(CHUNKS_PER_TICK * 20, GREEN, BOLD))
                                .appendSpace()
                                .appendSpace()
                                .appendSpace()
                                .append(text("%.1f%%".formatted(progress), GREEN))
                                .appendSpace()
                                .appendSpace()
                                .appendSpace()
                                .append(text(
                                        formatDuration(
                                                between(this.start, now()).toMillis()),
                                        GREEN,
                                        BOLD))
                                .build());
            }
            this.currentX += 16;

            if (this.currentX > this.borderSize) {
                this.currentX = -this.borderSize;
                this.currentZ += 16;

                this.unloadRow(this.currentZ - 32);
            }

            if (this.currentZ > this.borderSize) {
                for (final Profile profile : this.profileService.getProfiles()) {
                    final Actionbar actionbar = profile.getActionbar();
                    actionbar.unregisterActionbarEntry(UltraHardcoreKey.key("preload"));

                    profile.sendPrefixedMessage("Le monde de la partie a été&a pré-généré&r en &a%s&r."
                            .formatted(formatDuration(between(this.start, now()).toMillis())));
                }

                this.world.setGameRuleValue("doMobSpawning", "true");

                for (int z = this.currentZ - 32; z <= this.borderSize; z += 16) {
                    this.unloadRow(z);
                }

                this.cancel();
                return;
            }
        }
    }

    private void unloadRow(final int blockZ) {
        final int chunkZ = blockZ >> 4;
        for (int x = -this.borderSize; x <= this.borderSize; x += 16) {
            this.world.unloadChunkRequest(x >> 4, chunkZ);
        }
    }
}
