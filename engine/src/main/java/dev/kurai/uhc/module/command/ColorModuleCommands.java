package dev.kurai.uhc.module.command;

import com.google.common.collect.Sets;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.menu.color.ColorMenu;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ColorModuleCommands {

    private final UltraHardcoreAPI ultraHardcore;

    public ColorModuleCommands(final UltraHardcoreAPI ultraHardcore) {
        this.ultraHardcore = ultraHardcore;
    }

    @SubCommand(@CommandMeta(name = "color", description = "Change la couleur de joueurs"))
    public void color(final Player player, final @Argument(name = "joueurs") Player[] players) {
        final var holder = this.ultraHardcore.nicknameService().holder(player.getUniqueId());
        new ColorMenu(player, holder, Sets.newHashSet(players).stream().toList()).open();
    }
}
