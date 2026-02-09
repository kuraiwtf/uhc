package dev.kurai.uhc.util;

import static java.lang.String.format;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;

public final class TimeUtil {

  public static @NotNull String formatDuration(final long time) {
    final var duration = Duration.ofMillis(time);
    final long hours = duration.toHours();
    final long minutes = duration.toMinutesPart();
    final long seconds = duration.toSecondsPart();

    return hours > 0
        ? format("%02d:%02d:%02d", hours, minutes, seconds)
        : format("%02d:%02d", minutes, seconds);
  }
}
