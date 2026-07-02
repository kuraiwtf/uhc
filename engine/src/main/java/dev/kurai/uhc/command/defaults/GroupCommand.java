package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import org.bukkit.entity.Player;

@Command(@CommandMeta(name = "group", aliases = "g"))
public final class GroupCommand {

  @SubCommand(@CommandMeta(name = "set"))
  public void set(final Player player, final @Argument(name = "groupes") int groups) {
    // TODO:
  }

  @SubCommand(@CommandMeta(name = "alert"))
  public void alert(final Player player) {
    // TODO:
  }
}
