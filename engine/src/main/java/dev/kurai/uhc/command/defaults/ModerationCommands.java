package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.menu.list.PlayerListMenu;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.profile.Profile;
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
    if (!this.ultraHardcore.gameService().hostService().isHost(player)) {
      player.sendMessage(CC.prefix("Vous n'avez pas la permission d'effectuer cette action."));
      return;
    }

    new PlayerListMenu(player, this.ultraHardcore.profileService()).open();
  }

  @Command(@CommandMeta(name = "resetcd"))
  public void resetCooldown(
      final Player player, final @Argument(name = "joueur", defaultValue = "self") Player target) {
    if (!this.ultraHardcore.gameService().hostService().isHost(player)) {
      player.sendMessage(CC.prefix("Vous n'avez pas la permission d'effectuer cette action."));
      return;
    }

    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(target);
    for (final AbstractPower power : profile.getPowers()) {
      power
          .findOptionalRestriction(CooldownPowerRestriction.class, "cooldown")
          .ifPresent(restriction -> restriction.timeLeft(0));
    }

    player.sendMessage(
        CC.prefix(
            "Vous venez de&c réinitialiser&r les&b délais&r de&6 %s&r."
                .formatted(target.getName())));
  }
}
