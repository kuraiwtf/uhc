package dev.kurai.uhc.extension.minigame.command;

import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.extension.minigame.duel.DuelService;
import dev.kurai.uhc.extension.minigame.menu.DuelTypeSelectionMenu;
import dev.kurai.uhc.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class DuelCommand {

    private final DuelService duelService;

    @Command(@CommandMeta(name = "duel"))
    public void duel(final Player player, final @Argument(name = "target") Player target) {
        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(CC.prefix("§cVous ne pouvez pas vous défier."));
            return;
        }

        final DuelTypeSelectionMenu menu = new DuelTypeSelectionMenu(player, target, this.duelService);
        menu.open();
    }

    @Command(@CommandMeta(name = "duel-accept"))
    public void accept(final Player player, final @Argument(name = "target") Player target) {
        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(CC.prefix("§cVous ne pouvez pas vous défier."));
            return;
        }

        if (!this.duelService.hasRequest(target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage(CC.prefix("§cVous n'avez aucune demande en attente de §4" + target.getName() + "§c."));
            return;
        }

        this.duelService.acceptRequest(target, player);
    }
}
