package dev.kurai.uhc.game.episode;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EpisodeService {

    boolean isEnabled();

    void setEnabled(final boolean enabled);

    int getTimePerEpisode();

    void setTimePerEpisode(final int timePerEpisode);

    int getTimeLeft();

    void setTimeLeft(final int timeLeft);

    int getEpisode();

    void start();

    void stop();
}
