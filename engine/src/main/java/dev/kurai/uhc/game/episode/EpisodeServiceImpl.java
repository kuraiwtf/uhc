package dev.kurai.uhc.game.episode;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameEpisodeChangeEvent;
import java.time.Duration;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public final class EpisodeServiceImpl implements EpisodeService {

  private final UltraHardcoreAPI ultraHardcore;

  private int currentEpisode;
  private int timePerEpisode;
  private int timeLeft;

  private boolean enabled;

  public EpisodeServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;

    this.currentEpisode = 1;
    this.timePerEpisode = 20 * 60;
    this.timeLeft = this.timePerEpisode;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public int getTimePerEpisode() {
    return this.timePerEpisode;
  }

  @Override
  public void setTimePerEpisode(final int timePerEpisode) {
    this.timePerEpisode = timePerEpisode;
  }

  @Override
  public int getTimeLeft() {
    return this.timeLeft;
  }

  @Override
  public void setTimeLeft(final int timeLeft) {
    this.timeLeft = timeLeft;
  }

  @Override
  public int getEpisode() {
    return this.currentEpisode;
  }

  @Override
  public void start() {
    if (!this.enabled) {
      return;
    }

    this.timeLeft = this.timePerEpisode;
    final var service = EpisodeServiceImpl.this;
    new BukkitRunnable() {
      @Override
      public void run() {
        service.timeLeft--;

        if (service.timeLeft == 0) {
          service.timeLeft = service.timePerEpisode;
          service.currentEpisode++;
          Bukkit.getPluginManager().callEvent(new GameEpisodeChangeEvent(service.currentEpisode));

          final var gameService = EpisodeServiceImpl.this.ultraHardcore.gameService();
          gameService.showTitle(
              Title.title(
                  Component.empty(),
                  Component.text("Épisode ", NamedTextColor.AQUA)
                      .append(Component.text(service.currentEpisode)),
                  Title.Times.times(Duration.ZERO, Duration.ofSeconds(3L), Duration.ZERO)));

          gameService.playSound(Sound.sound(Key.key("note.pling"), Sound.Source.NEUTRAL, 1f, 1f));
        }
      }
    }.runTaskTimer(this.ultraHardcore.plugin(), 20L, 20L);
  }
}
