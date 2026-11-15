package dev.kurai.uhc.scoreboard.sidebar.adapter.builtin;

import static dev.kurai.uhc.util.GlobalUtil.getArrow;
import static dev.kurai.uhc.util.TimeUtil.formatDuration;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.group.GroupProvider;
import dev.kurai.uhc.game.group.GroupService;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.scoreboard.sidebar.SidebarAdapter;
import dev.kurai.uhc.timer.TimerService;
import dev.kurai.uhc.timer.builtin.BorderTimer;
import dev.kurai.uhc.timer.builtin.PvPTimer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class PlayingSidebarAdapter implements SidebarAdapter {

  private final UltraHardcoreAPI ultraHardcore;

  public PlayingSidebarAdapter(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  private static final long SNAPSHOT_TTL_MILLIS = 500L;

  private volatile long snapshotTime;
  private volatile int aliveCount;
  private volatile long teamsAlive;

  private void refreshSnapshot() {
    final long now = System.currentTimeMillis();
    if (now - this.snapshotTime < SNAPSHOT_TTL_MILLIS) {
      return;
    }

    final var alive =
        this.ultraHardcore
            .profileService()
            .getProfiles(profile -> profile.getState() instanceof PlayingProfileState);
    final var teamService = this.ultraHardcore.teamService();
    this.aliveCount = alive.size();
    this.teamsAlive =
        alive.stream()
            .map(profile ->
                teamService
                    .teamOf(profile.getId())
                    .<Object>map(team -> team)
                    .orElse(profile.getId()))
            .distinct()
            .count();
    this.snapshotTime = now;
  }

  @Override
  public List<Component> provideLines(final Player player) {
    final var lines = Lists.<Component>newArrayList();
    lines.add(empty());
    this.refreshSnapshot();
    final var playersLine =
        text()
            .append(SEPARATOR)
            .append(text("Joueurs: ", GRAY))
            .append(text(this.aliveCount, GOLD));

    final var teamService = this.ultraHardcore.teamService();
    if (teamService.enabled()) {
      playersLine
          .appendSpace()
          .append(text('(', DARK_GRAY))
          .append(text(this.teamsAlive, GOLD))
          .append(text(')', DARK_GRAY));
    }
    lines.add(playersLine.build());

    final GroupService groupService = this.ultraHardcore.gameService().groupService();
    final GroupProvider groupProvider = groupService.provider();
    if (groupService.enabled()) {
      lines.add(
          text()
              .append(SEPARATOR)
              .append(text("Groupes: ", GRAY))
              .append(text(groupProvider.groups(), YELLOW))
              .build());
    }

    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    if (profile.kills() > 0 || profile.assists() > 0) {
      lines.add(
          text()
              .append(SEPARATOR)
              .append(text("Kills: ", GRAY))
              .append(text(profile.kills(), RED))
              .appendSpace()
              .append(text('(', GRAY))
              .append(text(profile.assists(), RED))
              .append(text(')', GRAY))
              .build());
    }

    lines.add(
        text()
            .append(SEPARATOR)
            .append(text("Durée: ", GRAY))
            .append(
                text(formatDuration(this.ultraHardcore.gameService().duration() * 1000L), GREEN))
            .build());

    lines.add(empty());

    final AtomicBoolean hasTimerSection = new AtomicBoolean(false);
    final TimerService timerService = this.ultraHardcore.gameService().timerService();
    timerService
        .getTimer(PvPTimer.class)
        .ifPresent(timer -> {
          if (timer.isRunning()) {
            lines.add(
                text()
                    .append(SEPARATOR)
                    .append(text("PvP: ", GRAY))
                    .append(text(formatDuration(timer.getTimeLeft() * 1000L), GOLD))
                    .build());
            hasTimerSection.set(true);
          }
        });

    timerService
        .getTimer(BorderTimer.class)
        .ifPresent(timer -> {
          if (timer.isRunning()) {
            lines.add(
                text()
                    .append(SEPARATOR)
                    .append(text("Bordure: ", GRAY))
                    .append(text(formatDuration(timer.getTimeLeft() * 1000L), GOLD))
                    .build());
            hasTimerSection.set(true);
          }
        });

    final var module = this.ultraHardcore.moduleService().getCurrentModule();
    if (module instanceof final SidebarAdapter moduleSidebar) {
      final var moduleSidebarLines = moduleSidebar.provideLines(player);
      if (!moduleSidebarLines.isEmpty()) {
        lines.addAll(moduleSidebarLines);
        hasTimerSection.set(true);
      }
    }

    if (hasTimerSection.get()) {
      lines.add(empty());
    }

    lines.add(
        text()
            .append(SEPARATOR)
            .append(text("Bordure: ", GRAY))
            .append(text('±', LIGHT_PURPLE, BOLD))
            .append(
                text(
                    "%.1f".formatted(player.getWorld().getWorldBorder().getSize() / 2),
                    LIGHT_PURPLE))
            .build());

    final var centerLocation =
        new Location(player.getWorld(), 0.5, player.getLocation().getY(), 0.5);

    lines.add(
        text()
            .append(SEPARATOR)
            .append(text("Centre: ", GRAY))
            .append(
                text(getArrow(player.getLocation().clone(), centerLocation.clone()), LIGHT_PURPLE))
            .appendSpace()
            .append(text((int) centerLocation.distance(player.getLocation()), LIGHT_PURPLE))
            .append(text('m', LIGHT_PURPLE))
            .build());
    lines.add(empty());
    lines.add(this.ultraHardcore.moduleService().getCurrentModule().sidebarCredit());
    return lines;
  }
}
