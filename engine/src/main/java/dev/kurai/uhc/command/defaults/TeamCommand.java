package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.team.Team;
import dev.kurai.uhc.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static dev.kurai.uhc.util.CC.prefix;

@Command(@CommandMeta(name = "team", aliases = "equipe"))
@RequiredArgsConstructor
public final class TeamCommand {

    private final UltraHardcoreAPI ultraHardcore;

    private TeamService teams() {
        return this.ultraHardcore.teamService();
    }

    private boolean editable(final Player player) {
        if (!this.teams().enabled()) {
            player.sendMessage(prefix("&cLes équipes ne sont pas activées dans ce jeu."));
            return false;
        }

        if (this.ultraHardcore.moduleService().getCurrentModule() instanceof final TeamModule module
                && module.randomTeam()) {
            player.sendMessage(prefix("&cLes équipes sont aléatoires, vous ne pouvez pas les modifier."));
            return false;
        }

        if (this.ultraHardcore.gameService().playing()) {
            player.sendMessage(prefix("&cVous ne pouvez plus modifier les équipes."));
            return false;
        }

        return true;
    }

    @SubCommand(@CommandMeta(name = "create", description = "Créer une équipe"))
    public void create(final Player player) {
        if (!this.editable(player)) {
            return;
        }

        this.teams()
                .createTeam(player.getUniqueId())
                .ifPresentOrElse(
                        team -> player.sendMessage(prefix("Vous avez créé l'équipe " + team.coloredName() + "&r.")),
                        () -> player.sendMessage(prefix("&cVous êtes déjà dans une équipe.")));
    }

    @SubCommand(@CommandMeta(name = "invite", description = "Inviter un joueur dans votre équipe"))
    public void invite(final Player player, final @Argument(name = "joueur") Player target) {
        if (!this.editable(player)) {
            return;
        }

        final var team = this.teams().teamOf(player.getUniqueId()).orElse(null);
        if (team == null) {
            player.sendMessage(prefix("&cVous n'avez pas d'équipe. Utilisez &e/team create&c."));
            return;
        }

        if (!this.teams().invite(team, target.getUniqueId())) {
            player.sendMessage(
                    prefix("&cImpossible d'inviter&e " + target.getName() + "&c (équipe pleine ou déjà en équipe)."));
            return;
        }

        player.sendMessage(prefix("Vous avez invité&d " + target.getName() + "&r."));
        target.sendMessage(prefix("&d"
                + player.getName()
                + "&r vous invite dans son équipe. Faites&e /team accept "
                + player.getName()
                + "&r pour accepter."));
    }

    @SubCommand(@CommandMeta(name = "accept", description = "Accepter une invitation d'équipe"))
    public void accept(final Player player, final @Argument(name = "joueur") Player inviter) {
        if (!this.editable(player)) {
            return;
        }

        final var team = this.teams()
                .pendingInvite(player.getUniqueId(), inviter.getUniqueId())
                .orElse(null);
        if (team == null) {
            player.sendMessage(prefix("&cAucune invitation de&e " + inviter.getName() + "&c."));
            return;
        }

        if (!this.teams().join(team, player.getUniqueId())) {
            player.sendMessage(prefix("&cImpossible de rejoindre cette équipe."));
            return;
        }

        this.broadcast(team, "&d" + player.getName() + "&r a rejoint l'équipe.");
    }

    @SubCommand(@CommandMeta(name = "leave", description = "Quitter votre équipe"))
    public void leave(final Player player) {
        if (!this.editable(player)) {
            return;
        }

        final var team = this.teams().teamOf(player.getUniqueId()).orElse(null);
        if (team == null) {
            player.sendMessage(prefix("&cVous n'avez pas d'équipe."));
            return;
        }

        this.teams().leave(player.getUniqueId());
        player.sendMessage(prefix("Vous avez quitté votre équipe."));
        this.broadcast(team, "&d" + player.getName() + "&r a quitté l'équipe.");
    }

    @SubCommand(@CommandMeta(name = "list", description = "Lister les membres de votre équipe"))
    public void list(final Player player) {
        final var team = this.teams().teamOf(player.getUniqueId()).orElse(null);
        if (team == null) {
            player.sendMessage(prefix("&cVous n'avez pas d'équipe."));
            return;
        }

        player.sendMessage(prefix(team.coloredName()
                + "&r : "
                + team.members().stream()
                        .map(Bukkit::getOfflinePlayer)
                        .map(offline -> "&d" + offline.getName())
                        .collect(Collectors.joining("§f, "))
                + "§f."));
    }

    private void broadcast(final Team team, final String message) {
        for (final var member : team.members()) {
            final var online = Bukkit.getPlayer(member);
            if (online != null) {
                online.sendMessage(prefix(message));
            }
        }
    }
}
