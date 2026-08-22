package dev.kurai.uhc.extension.mumble.command;

import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.extension.mumble.MumbleExtension;
import org.bukkit.entity.Player;

@Command(@CommandMeta(name = "mumble", aliases = "mb"))
public final class MumbleCommand {

  private final MumbleExtension extension;

  public MumbleCommand(final MumbleExtension extension) {
    this.extension = extension;
  }

  @SubCommand(@CommandMeta(name = "join"))
  public void join(final Player player) {
    this.extension.advertise(player);
  }

  @SubCommand(@CommandMeta(name = "list"))
  public void list(final Player player) {
    player.sendMessage("todo...");
  }
}
