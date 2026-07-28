package dev.kurai.uhc.win;

import org.bukkit.Location;

public interface WinCelebration<I extends WinInformation> {

  void celebrate(final Location location, final I information);
}
