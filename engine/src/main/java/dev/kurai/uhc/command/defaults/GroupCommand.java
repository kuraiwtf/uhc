package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.game.group.GroupService;
import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.CC;
import org.bukkit.entity.Player;

@Command(@CommandMeta(name = "group", aliases = "g"))
public final class GroupCommand {

    private final GroupService groupService;
    private final ProfileService profileService;
    private final HostService hostService;

    public GroupCommand(
            final GroupService groupService, final ProfileService profileService, final HostService hostService) {
        this.groupService = groupService;
        this.profileService = profileService;
        this.hostService = hostService;
    }

    @SubCommand(@CommandMeta(name = "set", description = "Définir la limite de groupes"))
    public void set(final Player player, final @Argument(name = "groupes") int groups) {
        if (!this.hostService.hasHostAccess(player)) {
            player.sendMessage(CC.MISSING_PERMISSION);
            return;
        }

        if (!this.groupService.enabled()) {
            return;
        }

        if (groups < 1) {
            player.sendMessage(CC.prefix("&cLa limite de groupes doit être d'au moins 1."));
            return;
        }

        this.groupService.provider().groups(groups);
        this.alert(player);
        player.sendMessage(CC.prefix("Vous venez de définir les&d groupes&r de la partie à&d %d&r.".formatted(groups)));
    }

    @SubCommand(@CommandMeta(name = "alert", description = "Alerter de la limite des groupes"))
    public void alert(final Player player) {
        if (!this.hostService.hasHostAccess(player)) {
            player.sendMessage(CC.MISSING_PERMISSION);
            return;
        }

        this.groupService.alertGroups();
    }
}
