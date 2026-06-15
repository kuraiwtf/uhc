package dev.kurai.uhc.command.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.menu.claim.ClaimMenu;
import dev.kurai.uhc.menu.scenario.ScenarioViewMenu;
import dev.kurai.uhc.profile.component.ClaimComponent;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PlayerCommands {

  private final UltraHardcoreAPI ultraHardcore;

  public PlayerCommands(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Command(@CommandMeta(name = "claim", aliases = "full"))
  public void claim(final Player player) {
    final var profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    if (profile == null || !profile.hasComponent(ClaimComponent.class)) {
      return;
    }

    final var claim = profile.getComponent(ClaimComponent.class);
    if (claim.items().isEmpty()) {
      profile.sendMessage(
          prefix().append(text("Vous n'avez aucun objet à récupérer pour le moment.")).build());
      return;
    }

    new ClaimMenu(player, claim).open();
  }

  @Command(
      @CommandMeta(
          name = "uhc",
          aliases = {"rules", "regles"},
          description = "uhc.command.rules.description"))
  public void rules(final Player player) {}

  @Command(
      @CommandMeta(
          name = "scenarios",
          aliases = "scen",
          description = "uhc.command.scenarios.description"))
  public void scenarios(final Player player) {
    new ScenarioViewMenu(player, this.ultraHardcore.gameService().scenarioService()).open();
  }
}
