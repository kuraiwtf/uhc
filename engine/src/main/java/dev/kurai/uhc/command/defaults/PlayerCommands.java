package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.game.scenario.service.ScenarioService;
import dev.kurai.uhc.menu.scenario.ScenarioViewMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerCommands {

  private final ScenarioService scenarioService;

  public PlayerCommands(final @NotNull ScenarioService scenarioService) {
    this.scenarioService = scenarioService;
  }

  @Command(
      @CommandMeta(
          name = "uhc",
          aliases = {"rules", "regles"},
          description = "uhc.command.rules.description"))
  public void rules(final @NotNull Player player) {}

  @Command(
      @CommandMeta(
          name = "scenarios",
          aliases = "scen",
          description = "uhc.command.scenarios.description"))
  public void scenarios(final @NotNull Player player) {
    new ScenarioViewMenu(player, this.scenarioService).open();
  }
}
