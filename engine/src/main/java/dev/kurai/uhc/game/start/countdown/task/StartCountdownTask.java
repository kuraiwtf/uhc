package dev.kurai.uhc.game.start.countdown.task;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.game.scatter.service.ScatterService;
import java.time.Duration;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class StartCountdownTask extends BukkitRunnable {

  private static final int COUNTDOWN_TIME = 10;

  private final BukkitAudiences bukkitAudiences;
  private final ScatterService scatterService;
  private int timeLeft;

  public StartCountdownTask(
      final @NotNull BukkitAudiences bukkitAudiences,
      final @NotNull ScatterService scatterService) {
    this.bukkitAudiences = bukkitAudiences;
    this.scatterService = scatterService;
    this.timeLeft = COUNTDOWN_TIME;
  }

  @Override
  public void run() {
    if (this.timeLeft == 0) {
      this.scatterService.handleScatter();
      this.cancel();
      return;
    }

    if (this.timeLeft <= 5 || this.timeLeft == 10) {
      for (final var player : Bukkit.getOnlinePlayers()) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
      }

      this.bukkitAudiences
          .all()
          .showTitle(
              Title.title(
                  text(this.timeLeft, NamedTextColor.GREEN),
                  empty(),
                  Title.Times.times(Duration.ZERO, Duration.ofSeconds(3L), Duration.ZERO)));
    }

    this.timeLeft--;
  }
}
