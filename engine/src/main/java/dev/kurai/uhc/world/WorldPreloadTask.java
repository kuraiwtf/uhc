package dev.kurai.uhc.world;

import static net.kyori.adventure.text.Component.*;

import dev.kurai.actionbar.Actionbar;
import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.TimeUtil;
import java.time.Duration;
import java.time.Instant;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public final class WorldPreloadTask extends BukkitRunnable {

  private static final int CHUNKS_PER_TICK = 8;

  private final ProfileService profileService;

  private final World world;
  private final int borderSize;

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

    this.start = Instant.now();
  }

  @Override
  public void run() {
    for (int i = 0; i <= CHUNKS_PER_TICK; i++) {
      this.world.getChunkAtAsync(this.currentX >> 4, this.currentZ >> 4, Chunk::load);

      for (final Profile profile : this.profileService.getProfiles()) {
        final Actionbar actionbar = profile.getActionbar();
        actionbar.registerEntry(
            UltraHardcoreKey.key("preload"),
            text()
                .append(text("X: "))
                .append(text(this.currentX, NamedTextColor.GREEN))
                .appendSpace()
                .appendSpace()
                .appendSpace()
                .append(text("Z: "))
                .append(text(this.currentZ, NamedTextColor.GREEN))
                .appendSpace()
                .appendSpace()
                .appendSpace()
                .append(text("Chunk/tick: "))
                .append(text(CHUNKS_PER_TICK, NamedTextColor.GREEN))
                .appendSpace()
                .append(text('(', NamedTextColor.DARK_GRAY))
                .append(text(CHUNKS_PER_TICK * 20, NamedTextColor.GREEN, TextDecoration.BOLD))
                .append(text(')', NamedTextColor.DARK_GRAY))
                .appendSpace()
                .appendSpace()
                .appendSpace()
                .append(
                    text(
                        TimeUtil.formatDuration(
                            Duration.between(this.start, Instant.now()).toMillis()),
                        NamedTextColor.GREEN,
                        TextDecoration.BOLD))
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
          actionbar.unregisterEntry(UltraHardcoreKey.key("preload"));

          profile.sendPrefixedMessage(
              "Le monde de la partie a été&a pré-généré&r en &a%s&r."
                  .formatted(
                      TimeUtil.formatDuration(
                          Duration.between(this.start, Instant.now()).toMillis())));
        }

        this.cancel();
        return;
      }
    }
  }
}
