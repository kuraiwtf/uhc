package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.helpop.HelpOpTicket;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.util.CC;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(
    @CommandMeta(
        name = "helpopanswer",
        aliases = {"hopa", "ha"}))
public final class AnswerCommand {

  private final UltraHardcoreAPI ultraHardcore;

  @SubCommand(@CommandMeta(name = "tp"))
  public void tp(final Player player, final @Argument(name = "id") int id) {
    if (!this.hasAccess(player)) {
      return;
    }

    final var ticketOptional = this.resolveTicket(player, id);
    if (ticketOptional.isEmpty()) {
      return;
    }

    final var target = Bukkit.getPlayer(ticketOptional.get().askerId());
    if (target == null) {
      player.sendMessage(CC.prefix("Ce joueur n'est&c plus en ligne&r."));
      return;
    }

    player.teleport(target.getLocation());
    player.sendMessage(
        CC.prefix("Vous avez été téléporté au joueur du ticket&6 #%s&r.".formatted(id)));
  }

  @SubCommand(@CommandMeta(name = "inventaire"))
  public void inventory(final Player player, final @Argument(name = "id") int id) {
    if (!this.hasAccess(player)) {
      return;
    }

    final var ticketOptional = this.resolveTicket(player, id);
    if (ticketOptional.isEmpty()) {
      return;
    }

    final var target = Bukkit.getPlayer(ticketOptional.get().askerId());
    if (target == null) {
      player.sendMessage(CC.prefix("Ce joueur n'est&c plus en ligne&r."));
      return;
    }

    player.openInventory(target.getInventory());
  }

  @SubCommand(@CommandMeta(name = "repondre"))
  public void reply(
      final Player player,
      final @Argument(name = "id") int id,
      final @Argument(name = "message") String[] message) {
    if (!this.hasAccess(player)) {
      return;
    }

    final var ticketOptional = this.resolveTicket(player, id);
    if (ticketOptional.isEmpty()) {
      return;
    }

    final var target = Bukkit.getPlayer(ticketOptional.get().askerId());
    if (target == null) {
      player.sendMessage(CC.prefix("Ce joueur n'est&c plus en ligne&r."));
      return;
    }

    final var response = String.join(" ", message);
    target.sendMessage(
        CC.prefix(
            "&d[HelpOp #%s]&r Un membre de l'équipe vous répond&7:&r %s".formatted(id, response)));
    player.sendMessage(CC.prefix("Votre réponse a été envoyée au ticket&6 #%s&r.".formatted(id)));
  }

  @SubCommand(@CommandMeta(name = "qui"))
  public void who(final Player player, final @Argument(name = "id") int id) {
    if (!this.hasAccess(player)) {
      return;
    }

    final var ticketOptional = this.resolveTicket(player, id);
    if (ticketOptional.isEmpty()) {
      return;
    }

    player.sendMessage(
        CC.prefix(
            "Le ticket&6 #%s&r a été posé par&6 %s&r."
                .formatted(id, ticketOptional.get().askerName())));
  }

  @SubCommand(@CommandMeta(name = "liste"))
  public void list(final Player player) {
    if (!this.hasAccess(player)) {
      return;
    }

    final var tickets = this.ultraHardcore.helpOpService().getTickets();
    if (tickets.isEmpty()) {
      player.sendMessage(CC.prefix("Il n'y a aucune question en attente."));
      return;
    }

    player.sendMessage(CC.prefix("Voici la liste des questions en attente&7:"));
    for (final var ticket : tickets) {
      player.sendMessage(
          CC.colorize("&8 -&r &6#%s&r: %s".formatted(ticket.id(), ticket.question())));
    }
  }

  private boolean hasAccess(final Player player) {
    final var hostService = this.ultraHardcore.gameService().hostService();
    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    if (hostService.hasHostAccess(player) || profile.hasComponent(SpectatorComponent.class)) {
      return true;
    }

    player.sendMessage(CC.prefix("Vous n'avez pas la permission d'effectuer cette action."));
    return false;
  }

  private Optional<HelpOpTicket> resolveTicket(final Player player, final int id) {
    final var ticket = this.ultraHardcore.helpOpService().getTicket(id);
    if (ticket.isEmpty()) {
      player.sendMessage(CC.prefix("Le ticket&6 #%s&r n'existe pas.".formatted(id)));
    }
    return ticket;
  }
}
