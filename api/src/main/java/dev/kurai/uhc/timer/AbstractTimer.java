package dev.kurai.uhc.timer;

import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTimer {

  private final String identifier;
  private final String name;
  private final int minDuration;
  private final int defaultDuration;
  private final int maxDuration;

  private BukkitTask task;
  private int timeLeft;

  protected AbstractTimer() {
    final var clazz = this.getClass();

    if (!clazz.isAnnotationPresent(Identifier.class)) {
      throw new IllegalStateException(
          "Timer class " + clazz.getName() + " must be annotated with @Identifier");
    }
    this.identifier = clazz.getAnnotation(Identifier.class).value();

    if (!clazz.isAnnotationPresent(Name.class)) {
      throw new IllegalStateException(
          "Timer class " + clazz.getName() + " must be annotated with @Name");
    }
    this.name = clazz.getAnnotation(Name.class).value();

    if (clazz.isAnnotationPresent(Duration.class)) {
      final var duration = clazz.getAnnotation(Duration.class);
      this.minDuration = duration.min();
      this.defaultDuration = duration.defaultValue();
      this.maxDuration = duration.max();
    } else {
      this.minDuration = 0;
      this.defaultDuration = -1;
      this.maxDuration = -1;
    }

    this.timeLeft = this.defaultDuration;
    this.validateDurations();
  }

  private void validateDurations() {
    if (this.minDuration < 0) {
      throw new IllegalStateException(
          "Timer '"
              + this.identifier
              + "': minDuration cannot be negative (got "
              + this.minDuration
              + ")");
    }

    if (this.maxDuration != -1 && this.maxDuration < this.minDuration) {
      throw new IllegalStateException(
          "Timer '"
              + this.identifier
              + "': maxDuration ("
              + this.maxDuration
              + ") cannot be less than minDuration ("
              + this.minDuration
              + ")");
    }

    if (this.defaultDuration != -1 && this.defaultDuration < this.minDuration) {
      throw new IllegalStateException(
          "Timer '"
              + this.identifier
              + "': defaultDuration ("
              + this.defaultDuration
              + ") cannot be less than minDuration ("
              + this.minDuration
              + ")");
    }

    if (this.defaultDuration != -1
        && this.maxDuration != -1
        && this.defaultDuration > this.maxDuration) {
      throw new IllegalStateException(
          "Timer '"
              + this.identifier
              + "': defaultDuration ("
              + this.defaultDuration
              + ") cannot be greater than maxDuration ("
              + this.maxDuration
              + ")");
    }
  }

  public final void start(final @NotNull Plugin plugin) {
    if (this.task != null) {
      return;
    }

    this.onStart();

    this.task =
        Bukkit.getScheduler()
            .runTaskTimer(
                plugin,
                () -> {
                  if (this.timeLeft > 0) {
                    this.timeLeft--;
                  }
                  this.onSecond();

                  if (this.timeLeft == 0) {
                    this.stop();
                  }
                },
                20L,
                20L);
  }

  public final void stop() {
    if (this.task != null) {
      this.task.cancel();
      this.task = null;
      this.onEnd();
    }
  }

  public final boolean isRunning() {
    return this.task != null;
  }

  public void onStart() {}

  public void onSecond() {}

  public void onEnd() {}

  public @NotNull String getIdentifier() {
    return this.identifier;
  }

  public @NotNull String getName() {
    return this.name;
  }

  public int getMinDuration() {
    return this.minDuration;
  }

  public int getDefaultDuration() {
    return this.defaultDuration;
  }

  public int getMaxDuration() {
    return this.maxDuration;
  }

  public int getRemainingSeconds() {
    return this.timeLeft;
  }

  public int getTimeLeft() {
    return this.timeLeft;
  }

  public void setTimeLeft(final int timeLeft) {
    this.timeLeft = timeLeft;
  }
}
