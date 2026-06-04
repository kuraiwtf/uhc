package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.menu.list.PlayerListMenu;
import dev.kurai.uhc.util.CC;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ModerationCommands {

  private final UltraHardcoreAPI ultraHardcore;

  public ModerationCommands(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Command(@CommandMeta(name = "list"))
  public void list(final Player player) {
    if (!player.isOp()) {
      player.sendMessage(CC.prefix("Vous n'avez pas la permission d'utiliser cette commande."));
      return;
    }

    new PlayerListMenu(player, this.ultraHardcore.profileService()).open();
  }
}
