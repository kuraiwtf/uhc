package dev.kurai.uhc.game.episode;

public interface EpisodeService {

  boolean isEnabled();

  void setEnabled(final boolean enabled);

  int getEpisode();

  void start();
}
