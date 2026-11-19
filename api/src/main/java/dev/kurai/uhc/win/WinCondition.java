package dev.kurai.uhc.win;

import dev.kurai.uhc.profile.Profile;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public interface WinCondition {

    @Nullable
    Collection<Profile> validateWin();
}
