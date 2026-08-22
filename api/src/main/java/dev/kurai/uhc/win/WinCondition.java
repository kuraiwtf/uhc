package dev.kurai.uhc.win;

import dev.kurai.uhc.profile.Profile;
import java.util.Collection;
import org.jspecify.annotations.Nullable;

public interface WinCondition {

  @Nullable Collection<Profile> validateWin();
}
