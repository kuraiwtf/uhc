package dev.kurai.uhc.win;

import dev.kurai.uhc.profile.Profile;
import java.util.Collection;

public record BuiltinWinInformation(Collection<Profile> winners) implements WinInformation {}
