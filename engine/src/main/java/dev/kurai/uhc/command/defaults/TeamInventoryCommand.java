package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import static dev.kurai.uhc.util.CC.prefix;

@RequiredArgsConstructor
public final class TeamInventoryCommand {

    private final UltraHardcoreAPI ultraHardcore;

    @Command(@CommandMeta(name = "ti", description = "Ouvrir l'inventaire d'équipe"))
    public void inventory(final Player player) {
        final var teamService = this.ultraHardcore.teamService();
        if (!teamService.enabled()) {
            player.sendMessage(prefix("&cLes équipes ne sont pas activées dans ce jeu."));
            return;
        }

        if (!this.ultraHardcore.gameService().playing()) {
            player.sendMessage(prefix("&cL'inventaire d'équipe n'est accessible qu'une fois la partie commencée."));
            return;
        }

        final var team = teamService.teamOf(player.getUniqueId()).orElse(null);
        if (team == null) {
            player.sendMessage(prefix("&cVous n'avez pas d'équipe."));
            return;
        }

        final var inventory = team.inventory();
        if (inventory == null) {
            player.sendMessage(prefix("&cL'inventaire d'équipe est désactivé."));
            return;
        }

        player.openInventory(inventory);
    }
}
