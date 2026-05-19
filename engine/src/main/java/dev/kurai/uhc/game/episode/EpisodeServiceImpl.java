package dev.kurai.uhc.game.episode;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameEpisodeChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public final class EpisodeServiceImpl implements EpisodeService {

  private final UltraHardcoreAPI ultraHardcore;

  private int currentEpisode;
  private boolean enabled;

  public EpisodeServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;

    this.enabled = false;
    this.currentEpisode = 1;
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
      }
    }.runTaskTimer(this.ultraHardcore.getPlugin(), 20 * 20L, 20 * 20L);
  }
}
