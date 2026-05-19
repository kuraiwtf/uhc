package dev.kurai.uhc.game.episode;

public interface EpisodeService {

  void setEnabled(final boolean enabled);

  boolean isEnabled();

  int getEpisode();

  void start();
}
