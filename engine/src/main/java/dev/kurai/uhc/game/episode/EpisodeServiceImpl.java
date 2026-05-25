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
  private boolean enabled;

  public EpisodeServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
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

    final var service = EpisodeServiceImpl.this;
    new BukkitRunnable() {
      @Override
      public void run() {
        service.currentEpisode++;
        Bukkit.getPluginManager().callEvent(new GameEpisodeChangeEvent(service.currentEpisode));

        if (service.currentEpisode == 1) {
          return;
        }

        final var gameService = EpisodeServiceImpl.this.ultraHardcore.gameService();
        gameService.showTitle(
            Title.title(
                Component.empty(),
                Component.text("Épisode ", NamedTextColor.AQUA)
                    .append(Component.text(service.currentEpisode)),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(3L), Duration.ZERO)));

        gameService.playSound(Sound.sound(Key.key("note.pling"), Sound.Source.NEUTRAL, 1f, 1f));
      }
    }.runTaskTimer(this.ultraHardcore.plugin(), 0L, (20 * 60) * 20L);
  }
}
