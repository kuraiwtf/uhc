package dev.kurai.uhc.command.defaults;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.title.Title.Times.times;

import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.game.group.GroupService;
import dev.kurai.uhc.util.CC;
import java.time.Duration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(@CommandMeta(name = "group", aliases = "g"))
public final class GroupCommand {

  private final GroupService groupService;

  public GroupCommand(final GroupService groupService) {
    this.groupService = groupService;
  }

  @SubCommand(@CommandMeta(name = "set"))
  public void set(final Player player, final @Argument(name = "groupes") int groups) {
    if (!this.groupService.enabled()) {
      return;
    }

    this.groupService.provider().groups(groups);
    this.alert(player);
    player.sendMessage(
        CC.prefix(
            "Vous venez de définir les&d groupes&r de la partie à&d %d&r.".formatted(groups)));
  }

  @SubCommand(@CommandMeta(name = "alert"))
  public void alert(final Player player) {
    for (final Player receiver : Bukkit.getOnlinePlayers()) {
      receiver.showTitle(
          Title.title(
              text()
                  .append(text(CC.DANGER, DARK_RED))
                  .appendSpace()
                  .append(text("Groupes de " + this.groupService.provider().groups(), RED))
                  .appendSpace()
                  .append(text(CC.DANGER, DARK_RED))
                  .build(),
              empty(),
              times(Duration.ZERO, Duration.ofSeconds(3), Duration.ZERO)));
    }
  }
}
