package dev.kurai.uhc.team;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import com.google.common.collect.Sets;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.common.location.ApolloLocation;
import com.lunarclient.apollo.module.team.TeamMember;
import dev.kurai.uhc.adventure.UltraHardcoreKey;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.util.CC;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TeamDisplayTask implements Runnable {

  private static final Key ACTIONBAR_KEY = UltraHardcoreKey.key("team");
  private static final com.lunarclient.apollo.module.team.TeamModule LUNAR_MODULE =
      Apollo.getModuleManager().getModule(com.lunarclient.apollo.module.team.TeamModule.class);

  private final ProfileService profileService;
  private final TeamService teamService;
  private final ModuleService moduleService;

  private final Set<UUID> lunarActive = Sets.newHashSet();

  public TeamDisplayTask(
      final ProfileService profileService,
      final TeamService teamService,
      final ModuleService moduleService) {
    this.profileService = profileService;
    this.teamService = teamService;
    this.moduleService = moduleService;
  }

  @Override
  public void run() {
    final var display =
        this.moduleService.getCurrentModule() instanceof final TeamModule module
                && this.teamService.enabled()
            ? module.teamDisplay()
            : TeamDisplay.NONE;

    for (final var profile : this.profileService.getPlayingProfiles()) {
      final var player = profile.getPlayer();
      if (player == null) {
        continue;
      }

      final var team = this.teamService.teamOf(player.getUniqueId()).orElse(null);
      final var teammates =
          team == null
              ? List.<Player>of()
              : team.members().stream()
                  .filter(id -> !id.equals(player.getUniqueId()))
                  .map(Bukkit::getPlayer)
                  .filter(
                      mate ->
                          mate != null
                              && mate.isOnline()
                              && this.profileService.getOrCreateProfile(mate).getState()
                                  instanceof PlayingProfileState)
                  .toList();

      if (display.actionBar() && !teammates.isEmpty()) {
        profile
            .getActionbar()
            .registerActionbarEntry(ACTIONBAR_KEY, this.actionBar(player, team, teammates));
      } else {
        profile.getActionbar().unregisterActionbarEntry(ACTIONBAR_KEY);
      }

      if (display.lunarClient() && !teammates.isEmpty()) {
        this.updateLunar(player, team, teammates);
      } else {
        this.resetLunar(player);
      }
    }
  }

  private Component actionBar(final Player player, final Team team, final List<Player> teammates) {
    final TextComponent.Builder builder = text();
    for (int i = 0; i < teammates.size(); i++) {
      final var mate = teammates.get(i);
      if (i > 0) {
        builder.append(text(" " + CC.BAR + " ", DARK_GRAY));
      }

      final String distance =
          mate.getWorld().equals(player.getWorld())
              ? Math.round(mate.getLocation().distance(player.getLocation())) + "m"
              : "?";
      builder
          .append(text(mate.getName(), team.style().color().asTextColor()))
          .append(text(" " + Math.round(mate.getHealth() / 2), RED))
          .append(text('❤', DARK_RED))
          .append(text(" " + distance, GRAY));
    }
    return builder.build();
  }

  private void updateLunar(final Player player, final Team team, final List<Player> teammates) {
    Apollo.getPlayerManager()
        .getPlayer(player.getUniqueId())
        .ifPresent(
            apolloPlayer -> {
              LUNAR_MODULE.updateTeamMembers(
                  apolloPlayer,
                  teammates.stream()
                      .map(
                          mate ->
                              TeamMember.builder()
                                  .playerUuid(mate.getUniqueId())
                                  .displayName(
                                      text(mate.getName(), team.style().color().asTextColor()))
                                  .markerColor(team.style().color().asJavaColor())
                                  .location(
                                      ApolloLocation.builder()
                                          .world(mate.getWorld().getName())
                                          .x(mate.getLocation().getX())
                                          .y(mate.getLocation().getY())
                                          .z(mate.getLocation().getZ())
                                          .build())
                                  .build())
                      .toList());
              this.lunarActive.add(player.getUniqueId());
            });
  }

  private void resetLunar(final Player player) {
    if (!this.lunarActive.remove(player.getUniqueId())) {
      return;
    }

    Apollo.getPlayerManager()
        .getPlayer(player.getUniqueId())
        .ifPresent(LUNAR_MODULE::resetTeamMembers);
  }
}
