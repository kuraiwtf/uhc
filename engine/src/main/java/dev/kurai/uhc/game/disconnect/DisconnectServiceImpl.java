package dev.kurai.uhc.game.disconnect;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.death.DeathService;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
@Getter
@Setter
public final class DisconnectServiceImpl implements DisconnectService {

  private final UltraHardcoreAPI ultraHardcore;

  private long disconnectTime = 15 * 60 * 1_000L;
  private Strategy strategy = Strategy.CUMULATIVE;

  @Override
  public void start() {
    Bukkit.getScheduler()
        .runTaskTimer(
            this.ultraHardcore.plugin(),
            () -> {
              for (final Profile profile :
                  this.ultraHardcore
                      .profileService()
                      .getProfiles(
                          profile ->
                              profile.getState() instanceof PlayingProfileState
                                  && profile.findPlayer().isEmpty())) {
                final DisconnectComponent disconnectComponent =
                    profile.getComponent(DisconnectComponent.class);
                if (disconnectComponent == null) {
                  continue;
                }

                if (Duration.between(disconnectComponent.lastLogin(), Instant.now()).toMillis()
                        >= this.disconnectTime
                    || disconnectComponent.timeLeft() <= 0) {
                  final GameService gameService = this.ultraHardcore.gameService();
                  final DeathService deathService = gameService.deathService();
                  deathService.eliminate(profile, null, true);
                  gameService.sendMessage(
                      deathService.deathAnnounce().provideDeathMessage(profile, null, true));
                }
              }
            },
            0,
            1L);
  }
}
