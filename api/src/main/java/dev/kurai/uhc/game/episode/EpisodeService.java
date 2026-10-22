package dev.kurai.uhc.game.episode;

public interface EpisodeService {

  boolean isEnabled();

  void setEnabled(final boolean enabled);

  int getTimePerEpisode();

  void setTimePerEpisode(final int timePerEpisode);

  int getTimeLeft();

  void setTimeLeft(final int timeLeft);

  int getEpisode();

  void start();
}
