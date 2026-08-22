package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(@CommandMeta(name = "spectator", aliases = "spec"))
public final class SpectatorCommand {

  private static final Class<? extends Component> SPECTATOR_COMPONENT = SpectatorComponent.class;

  private final UltraHardcoreAPI ultraHardcore;

  @SubCommand(@CommandMeta(name = "list"))
  public void list(final Player player) {
    final var spectators =
        this.ultraHardcore
            .profileService()
            .getProfiles(profile -> profile.hasComponent(SPECTATOR_COMPONENT));

    if (spectators.isEmpty()) {
      player.sendMessage(CC.prefix("Il n'y a aucun&d spectateur&r pour le moment."));
      return;
    }

    player.sendMessage(CC.prefix("Voici la liste des&d spectateurs&r:"));
    for (final Profile profile : spectators) {
      player.sendMessage(CC.colorize("&8 -&r %s".formatted(profile.getName())));
    }
  }

  @SubCommand(@CommandMeta(name = "add"))
  public void add(final Player player, final @Argument(name = "joueur") Player target) {
    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(target);
    if (profile.hasComponent(SPECTATOR_COMPONENT)) {
      player.sendMessage(
          CC.prefix("&6%s&r est déjà un&d spectateur&r.".formatted(target.getName())));
      return;
    }

    profile.addComponent(new SpectatorComponent());
    player.sendMessage(
        CC.prefix(
            "Vous venez d'&aajouter&f le joueur&6 %s&r en tant que&d spectateur&r."
                .formatted(target.getName())));
  }

  @SubCommand(@CommandMeta(name = "remove"))
  public void remove(final Player player, final @Argument(name = "joueur") Player target) {
    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(target);
    if (!profile.hasComponent(SPECTATOR_COMPONENT)) {
      player.sendMessage(
          CC.prefix("&6%s&r n'est pas un&d spectateur&r.".formatted(target.getName())));
      return;
    }

    profile.removeComponent(SPECTATOR_COMPONENT);
    player.sendMessage(
        CC.prefix(
            "Vous venez de&c retirer&f le joueur&6 %s&r de la liste des&d spectateurs&r."
                .formatted(target.getName())));
  }
}
