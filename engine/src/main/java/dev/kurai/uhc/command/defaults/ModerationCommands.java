package dev.kurai.uhc.command.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.configuration.border.BorderConfiguration;
import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.menu.list.PlayerListMenu;
import dev.kurai.uhc.menu.spectator.InventoryViewMenu;
import dev.kurai.uhc.menu.spectator.TopLuckMenu;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.GlobalUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ModerationCommands {

  private final UltraHardcoreAPI ultraHardcore;

  public ModerationCommands(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Command(@CommandMeta(name = "disperse", description = "Disperser les joueurs"))
  public void disperse(
      final Player player,
      final @Argument(name = "rayon") int radius,
      final @Argument(name = "cible", defaultValue = "self") Player target) {
    if (!this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
      player.sendMessage(CC.MISSING_PERMISSION);
      return;
    }

    final var toTeleport =
        GlobalUtil.getPlayersAround(target.getLocation(), radius).stream()
            .filter(filtered -> !filtered.getUniqueId().equals(player.getUniqueId()))
            .toList();
    final var locations =
        Lists.newArrayList(
            this.ultraHardcore
                .gameService()
                .scatterService()
                .getPositionProvider()
                .provideLocations(
                    BorderConfiguration.FINAL_SIZE_OPTION.getValue(), toTeleport.size()));

    final var iterator = toTeleport.iterator();
    while (iterator.hasNext()) {
      final Location location = locations.removeFirst();
      final Player next = iterator.next();
      final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(next);
      profile.addDamageImmunityUntilNext(EntityDamageEvent.DamageCause.FALL);
      next.teleport(location);
      iterator.remove();
    }

    player.sendMessage(
        CC.prefix(
            "Vous avez dispersé un total de§d %d§f joueurs dans un rayon de §b%d blocs§f autour de §e%s§f."
                .formatted(locations.size(), radius, target.getName())));
  }

  @Command(@CommandMeta(name = "invsee", description = "Voir l'inventaire d'un joueur"))
  public void inventorySee(final Player player, final @Argument(name = "joueur") Player target) {
    final HostService hostService = this.ultraHardcore.gameService().hostService();
    final ProfileService profileService = this.ultraHardcore.profileService();
    final Profile profile = profileService.getOrCreateProfile(player);
    if (!hostService.hasHostAccess(player) && !profile.hasComponent(SpectatorComponent.class)) {
      player.sendMessage(CC.MISSING_PERMISSION);
      return;
    }

    new InventoryViewMenu(player, target, profileService.getOrCreateProfile(target)).open();
  }

  @Command(@CommandMeta(name = "topluck", description = "Voir le classement des minerais"))
  public void topLuck(final Player player) {
    final HostService hostService = this.ultraHardcore.gameService().hostService();
    final ProfileService profileService = this.ultraHardcore.profileService();
    final Profile profile = profileService.getOrCreateProfile(player);
    if (!hostService.hasHostAccess(player) && !profile.hasComponent(SpectatorComponent.class)) {
      player.sendMessage(CC.prefix(CC.MISSING_PERMISSION));
      return;
    }

    final GameService gameService = this.ultraHardcore.gameService();
    if (gameService.startTime() == 0L) {
      player.sendMessage(CC.prefix("La partie n'est pas en cours de jeu."));
      return;
    }

    new TopLuckMenu(player, profileService, TopLuckMenu.Sorting.DIAMOND).open();
  }

  @Command(@CommandMeta(name = "list"))
  public void list(final Player player) {
    if (!player.isOp()) {
      player.sendMessage(CC.MISSING_PERMISSION);
      return;
    }

    new PlayerListMenu(player, this.ultraHardcore.profileService()).open();
  }

  @Command(@CommandMeta(name = "resetcd"))
  public void resetCooldown(
      final Player player, final @Argument(name = "joueur", defaultValue = "self") Player target) {
    if (!this.ultraHardcore.gameService().hostService().isHost(player) || !player.isOp()) {
      player.sendMessage(CC.MISSING_PERMISSION);
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
