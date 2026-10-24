package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.DefaultCommand;
import dev.kurai.uhc.command.annotation.SubCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Command(
    @CommandMeta(
        name = "uhc",
        aliases = {"rules", "regles"},
        description = "uhc.command.rules.description"))
public final class UltraHardcoreCommand {

  private final UltraHardcoreAPI ultraHardcore;

  public UltraHardcoreCommand(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @DefaultCommand
  public void rules(final Player player) {
    //    new RulesMenu(player, this.ultraHardcore).open();
  }

  @SubCommand(
      @CommandMeta(
          name = "loadtexts",
          permission = "uhc.command.loadtexts",
          description = "Charge les textes"))
  public void loadTexts(final Player player) {}
}
