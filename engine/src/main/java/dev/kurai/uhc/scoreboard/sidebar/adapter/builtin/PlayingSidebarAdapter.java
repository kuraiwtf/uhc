package dev.kurai.uhc.scoreboard.sidebar.adapter.builtin;

import static dev.kurai.uhc.util.GlobalUtil.getArrow;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.scoreboard.sidebar.SidebarAdapter;
import dev.kurai.uhc.util.TimeUtil;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PlayingSidebarAdapter implements SidebarAdapter {

  private final UltraHardcoreAPI ultraHardcore;

  public PlayingSidebarAdapter(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public List<Component> provideLines(final Player player) {
    final var lines = Lists.<Component>newArrayList();
    lines.add(empty());
    lines.add(
        text()
            .append(text("Joueurs: "))
            .append(
                text(
                    this.ultraHardcore
                        .profileService()
                        .getProfiles(profile -> profile.getState() instanceof PlayingProfileState)
                        .size(),
                    GOLD,
                    BOLD))
            .build());
    lines.add(
        text()
            .append(text("Temps: "))
            .append(
                text(
                    TimeUtil.formatDuration(
                        (System.currentTimeMillis()
                            - this.ultraHardcore.gameService().startTime())),
                    GOLD,
                    BOLD))
            .build());
    lines.add(empty());

    final var module = this.ultraHardcore.moduleService().getCurrentModule();
    if (module instanceof final SidebarAdapter moduleSidebar) {
      final var moduleSidebarLines = moduleSidebar.provideLines(player);
      if (!moduleSidebarLines.isEmpty()) {
        lines.addAll(moduleSidebarLines);
        lines.add(empty());
      }
    }

    lines.add(
        text()
            .append(text("Bordure: "))
            .append(text('±', GOLD, BOLD))
            .append(
                text(
                    "%.1f".formatted(player.getWorld().getWorldBorder().getSize() / 2), GOLD, BOLD))
            .build());

    final var centerLocation =
        new Location(player.getWorld(), 0.5, player.getLocation().getY(), 0.5);

    lines.add(
        text()
            .append(text("Centre: "))
            .append(text(getArrow(player.getLocation().clone(), centerLocation.clone()), GOLD))
            .appendSpace()
            .append(
                text("%.1f".formatted(centerLocation.distance(player.getLocation())), GOLD, BOLD))
            .append(text('m', GOLD))
            .build());
    lines.add(empty());
    lines.add(text().append(text('@', DARK_AQUA)).append(text("kuraiwtf", AQUA)).build());
    return lines;
  }
}
