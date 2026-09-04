package dev.kurai.uhc.command.defaults;

import static dev.kurai.uhc.util.CC.prefix;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.helpop.HelpOpTicket;
import dev.kurai.uhc.menu.spectator.InventoryViewMenu;
import dev.kurai.uhc.profile.Profile;
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
        aliases = {"hopa", "ha"},
        permission = "uhc.command.answer"))
public final class AnswerCommand {

  private final UltraHardcoreAPI ultraHardcore;

  @SubCommand(
      @CommandMeta(
          name = "tp",
          description = "Se téléporter à un joueur",
          permission = "uhc.command.answer.tp"))
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
      player.sendMessage(prefix("Ce joueur n'est&c plus en ligne&r."));
      return;
    }

    player.teleport(target.getLocation());
    player.sendMessage(
        prefix("Vous avez été téléporté au joueur du help-op&6 #%s&r.".formatted(id)));
  }

  @SubCommand(
      @CommandMeta(
          name = "inv",
          description = "Voir l'inventaire d'un joueur",
          permission = "uhc.command.answer.inv"))
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
      player.sendMessage(prefix("Ce joueur n'est&c plus en ligne&r."));
      return;
    }

    final Profile profile = this.ultraHardcore.profileService().getOrCreateProfile(target);
    new InventoryViewMenu(player, target, profile).open();
  }

  @SubCommand(
      @CommandMeta(
          name = "msg",
          description = "Répondre à un joueur",
          permission = "uhc.command.answer.reply"))
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
      player.sendMessage(prefix("Ce joueur n'est&c plus en ligne&r."));
      return;
    }

    final var response = String.join(" ", message);
    target.sendMessage(prefix("Un membre de l'équipe vous a répondu&7:&a %s".formatted(response)));
    player.sendMessage(prefix("Votre réponse a été envoyée au help-op&6 #%s&r.".formatted(id)));
  }

  @SubCommand(
      @CommandMeta(
          name = "whois",
          description = "Voir le pseudo à l'origine d'un helpop",
          permission = "uhc.command.answer.whois"))
  public void who(final Player player, final @Argument(name = "id") int id) {
    if (!this.hasAccess(player)) {
      return;
    }

    final var ticketOptional = this.resolveTicket(player, id);
    if (ticketOptional.isEmpty()) {
      return;
    }

    player.sendMessage(
        prefix(
            "Le joueur&6 %s&r a posé la question liée au help-op&6 #%s&r&r."
                .formatted(ticketOptional.get().askerName(), id)));
  }

  @SubCommand(
      @CommandMeta(
          name = "list",
          description = "Voir la liste des help-ops en attente",
          permission = "uhc.command.answer.list"))
  public void list(final Player player) {
    if (!this.hasAccess(player)) {
      return;
    }

    final var tickets = this.ultraHardcore.helpOpService().getTickets();
    if (tickets.isEmpty()) {
      player.sendMessage(prefix("Il n'y a aucune question en attente."));
      return;
    }

    player.sendMessage(prefix("Voici la liste des questions en attente&7:"));
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

    player.sendMessage(prefix("Vous n'avez pas la permission d'effectuer cette action."));
    return false;
  }

  private Optional<HelpOpTicket> resolveTicket(final Player player, final int id) {
    final var ticket = this.ultraHardcore.helpOpService().getTicket(id);
    if (ticket.isEmpty()) {
      player.sendMessage(prefix("La question&6 #%s&r n'existe pas.".formatted(id)));
    }
    return ticket;
  }
}
