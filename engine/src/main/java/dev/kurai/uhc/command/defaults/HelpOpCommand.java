package dev.kurai.uhc.command.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.helpop.HelpOpAction;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class HelpOpCommand {

  private final UltraHardcoreAPI ultraHardcore;

  @Command(
      @CommandMeta(
          name = "helpop",
          description = "Poser une question anonyme aux hôtes et spectateurs."))
  public void helpop(final Player player, final @Argument(name = "question") String[] question) {
    final var message = String.join(" ", question);
    final var ticket =
        this.ultraHardcore
            .helpOpService()
            .createTicket(player.getUniqueId(), player.getName(), message);

    player.sendMessage(prefix("Votre question a été envoyée au staff."));

    final var hostService = this.ultraHardcore.gameService().hostService();
    final var recipients =
        this.ultraHardcore
            .profileService()
            .getProfiles(
                profile ->
                    hostService.hasHostAccess(profile.getId())
                        || profile.hasComponent(SpectatorComponent.class));

    final var broadcast =
        text()
            .append(prefix())
            .append(text("[", DARK_GRAY))
            .append(text("HelpOp", LIGHT_PURPLE, TextDecoration.BOLD))
            .append(text(" #" + ticket.id(), GOLD))
            .append(text("] ", DARK_GRAY))
            .append(text(message, WHITE))
            .appendNewline()
            .appendSpace();

    for (final var action : this.ultraHardcore.helpOpService().getActions()) {
      broadcast.append(this.actionButton(action, ticket.id())).appendSpace();
    }

    final var built = broadcast.build();
    for (final var profile : recipients) {
      profile.sendMessage(built);
    }
  }

  private Component actionButton(final HelpOpAction action, final int ticketId) {
    final var command = action.command().apply(ticketId);
    return text()
        .append(text('[', DARK_GRAY))
        .append(text(action.label(), action.color()))
        .append(text(']', DARK_GRAY))
        .hoverEvent(HoverEvent.showText(text(action.hoverText())))
        .clickEvent(
            action.suggestOnly()
                ? ClickEvent.suggestCommand(command)
                : ClickEvent.runCommand(command))
        .build();
  }
}
