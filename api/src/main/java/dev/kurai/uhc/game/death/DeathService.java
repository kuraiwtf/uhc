package dev.kurai.uhc.game.death;

import dev.kurai.uhc.profile.Profile;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface DeathService {

  DeathAnnounce deathAnnounce();

  void deathAnnounce(final DeathAnnounce deathAnnounce);

  DeathProcessor deathProcessor();

  void deathProcessor(final DeathProcessor deathProcessor);

  void eliminate(final Profile profile, final @Nullable Profile killer, final boolean offline);
}
