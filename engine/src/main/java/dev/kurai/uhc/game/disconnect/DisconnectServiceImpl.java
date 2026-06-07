package dev.kurai.uhc.game.disconnect;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DisconnectComponent;
import dev.kurai.uhc.profile.component.PlayerInformationComponent;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.profile.state.SpectatingProfileState;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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

                final PlayerInformationComponent informationComponent =
                    profile.getComponent(PlayerInformationComponent.class);
                if (informationComponent == null) {
                  continue;
                }

                if (Duration.between(disconnectComponent.lastLogin(), Instant.now()).toMillis()
                    >= this.disconnectTime) {
                  final GameService gameService = this.ultraHardcore.gameService();
                  gameService.deathService().eliminate(profile, null, true);

                  profile.removeComponent(DisconnectComponent.class);
                  profile.setState(new SpectatingProfileState());

                  this.ultraHardcore
                      .gameService()
                      .sendMessage(
                          MiniMessage.miniMessage()
                              .deserialize(
                                  "<st><gold>-------</gold><yellow>-------</yellow><white>-------</white><yellow>-------</yellow><gold>-------</gold></st><newline><newline> <dark_gray>»</dark_gray> <b><name></b> est mort de <red>déconnexion</red>.<newline><newline><st><gold>-------</gold><yellow>-------</yellow><white>-------</white><yellow>-------</yellow><gold>-------</gold></st>",
                                  TagResolver.resolver(
                                      "name",
                                      Tag.inserting(
                                          Component.text(profile.getName(), WHITE, BOLD)))));
                }
              }
            },
            0,
            1L);
  }
}
