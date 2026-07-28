package dev.kurai.uhc.win;

import static dev.kurai.uhc.util.CC.center;
import static org.bukkit.Bukkit.broadcastMessage;

import dev.kurai.uhc.profile.Profile;
import org.bukkit.Location;

public final class BuiltinWinCelebration implements WinCelebration<WinInformation> {

  @Override
  public void celebrate(final Location location, final WinInformation information) {
    broadcastMessage("");
    broadcastMessage(center("&e&lVICTOIRE"));
    broadcastMessage(
        center(
            "Joueurs: &e"
                + String.join(
                    "&r, &e", information.winners().stream().map(Profile::getName).toList())
                + "&r."));
    broadcastMessage("");
  }
}
