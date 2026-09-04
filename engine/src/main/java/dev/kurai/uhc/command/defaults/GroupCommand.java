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
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.CC;
import java.time.Duration;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Command(@CommandMeta(name = "group", aliases = "g", permission = "uhc.command.group"))
public final class GroupCommand {

  private final GroupService groupService;
  private final ProfileService profileService;

  public GroupCommand(final GroupService groupService, final ProfileService profileService) {
    this.groupService = groupService;
    this.profileService = profileService;
  }

  @SubCommand(
      @CommandMeta(
          name = "set",
          description = "Définir la limite de groupes",
          permission = "uhc.command.group.set"))
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

  @SubCommand(
      @CommandMeta(
          name = "alert",
          description = "Alerter de la limite des groupes",
          permission = "uhc.command.group.alert"))
  public void alert(final Player player) {
    for (final Profile profile :
        this.profileService.getProfiles(profile -> profile.findPlayer().isPresent())) {
      profile.showTitle(
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

      profile
          .findPlayer()
          .ifPresent(
              receiver -> receiver.playSound(receiver.getLocation(), Sound.ANVIL_LAND, 1, 1));
    }
  }
}
