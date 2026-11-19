package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.menu.rules.enchantment.EnchantmentLimitPlayersMenu;
import dev.kurai.uhc.util.CC;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PlayersCommand {

    private final UltraHardcoreAPI ultraHardcore;

    public PlayersCommand(final UltraHardcoreAPI ultraHardcore) {
        this.ultraHardcore = ultraHardcore;
    }

    @Command(@CommandMeta(name = "players", description = "Modifier les limites d'enchantement des joueurs"))
    public void players(final Player player) {
        if (!this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
            player.sendMessage(CC.MISSING_PERMISSION);
            return;
        }

        new EnchantmentLimitPlayersMenu(
                        player,
                        this.ultraHardcore.profileService(),
                        this.ultraHardcore.gameService().enchantmentLimitService())
                .open();
    }
}
