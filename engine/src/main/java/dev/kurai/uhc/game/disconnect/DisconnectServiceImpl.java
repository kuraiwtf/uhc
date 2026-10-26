package dev.kurai.uhc.game.disconnect;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
@Getter
@Setter
public final class DisconnectServiceImpl implements DisconnectService {

  private final UltraHardcoreAPI ultraHardcore;

  private int disconnectTime = 15 * 60;
  private Strategy strategy = Strategy.CUMULATIVE;

  @Override
  public void start() {
    Bukkit.getScheduler()
        .runTaskTimer(
            this.ultraHardcore.plugin(),
            () -> {
              if (this.strategy == Strategy.DISABLED) {
                return;
              }

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

                disconnectComponent.timeLeft(disconnectComponent.timeLeft() - 1);
                if (disconnectComponent.timeLeft() <= 0) {
                  this.ultraHardcore.gameService().deathService().eliminate(profile, null, true);
                }
              }
            },
            0,
            20L);
  }
}
