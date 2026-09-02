package dev.kurai.uhc.world;

import static dev.kurai.uhc.util.TimeUtil.formatDuration;
import static java.time.Duration.between;
import static java.time.Instant.now;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import dev.kurai.actionbar.Actionbar;
import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import java.time.Instant;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public final class WorldPreloadTask extends BukkitRunnable {

  private static final int CHUNKS_PER_TICK = 10;

  private final ProfileService profileService;

  private final World world;
  private final int borderSize;

  private final int totalChunks;
  private int loadedChunks;

  private int currentX;
  private int currentZ;

  private final Instant start;

  public WorldPreloadTask(
      final ProfileService profileService, final World world, final int borderSize) {
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
    for (int i = 0; i <= CHUNKS_PER_TICK; i++) {
      this.world.getChunkAt(this.currentX >> 4, this.currentZ >> 4).load();
      this.loadedChunks++;
      final double progress = Math.min(100.0, (this.loadedChunks * 100.0) / this.totalChunks);

      for (final Profile profile : this.profileService.getProfiles()) {
        final Actionbar actionbar = profile.getActionbar();
        actionbar.registerActionbarEntry(
            UltraHardcoreKey.key("preload"),
            text()
                .append(text("C/s: "))
                .append(text(CHUNKS_PER_TICK * 20, GREEN, BOLD))
                .appendSpace()
                .appendSpace()
                .appendSpace()
                .append(text("%.1f%%".formatted(progress), GREEN))
                .appendSpace()
                .appendSpace()
                .appendSpace()
                .append(text(formatDuration(between(this.start, now()).toMillis()), GREEN, BOLD))
                .build());
      }
      this.currentX += 16;

      if (this.currentX > this.borderSize) {
        this.currentX = -this.borderSize;
        this.currentZ += 16;
      }

      if (this.currentZ > this.borderSize) {
        for (final Profile profile : this.profileService.getProfiles()) {
          final Actionbar actionbar = profile.getActionbar();
          actionbar.unregisterActionbarEntry(UltraHardcoreKey.key("preload"));

          profile.sendPrefixedMessage(
              "Le monde de la partie a été&a pré-généré&r en &a%s&r."
                  .formatted(formatDuration(between(this.start, now()).toMillis())));
        }

        this.cancel();
        return;
      }
    }
  }
}
