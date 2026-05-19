package dev.kurai.uhc.command.help;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import com.google.common.collect.Lists;
import dev.kurai.uhc.command.UltraHardcoreParentCommand;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

public final class HelpCommand {

  private static final Component SEPARATOR = CC.line(GOLD, YELLOW);
  private static final int COMMANDS_PER_PAGE = 5;

  private final UltraHardcoreParentCommand parentCommand;
  private final int totalPages;

  public HelpCommand(final @NotNull UltraHardcoreParentCommand parentCommand) {
    this.parentCommand = parentCommand;
    this.totalPages =
        (int) Math.ceil((double) parentCommand.getSubCommands().size() / COMMANDS_PER_PAGE);
  }

  public void display(final @NotNull Audience audience, final int page) {
    if (page < 1 || page > this.totalPages) {
      audience.sendMessage(
          text()
              .append(prefix())
              .appendSpace()
              .append(text("La page ", RED))
              .append(text(page, DARK_RED))
              .append(text(" n'existe pas.", RED))
              .build());
      return;
    }

    final var message =
        text()
            .append(SEPARATOR)
            .appendNewline()
            .appendSpace()
            .append(text("Aide: "))
            .append(text("/", GOLD))
            .append(text(this.parentCommand.getName(), YELLOW))
            .appendNewline()
            .appendNewline();

    final var subCommands = Lists.newArrayList(this.parentCommand.getSubCommands());
    final int fromIndex = (page - 1) * COMMANDS_PER_PAGE;
    final int toIndex = Math.min(fromIndex + COMMANDS_PER_PAGE, subCommands.size());
    final var pageCommands = subCommands.subList(fromIndex, toIndex);

    for (final var subCommand : pageCommands) {
      message
          .appendSpace()
          .append(text(CC.SQUARE, GOLD))
          .appendSpace()
          .append(text("/", GOLD))
          .append(text(this.parentCommand.getName(), YELLOW))
          .appendSpace()
          .append(text(subCommand.commandMeta().name(), YELLOW))
          .appendSpace()
          .append(text("-", GRAY))
          .appendSpace()
          .append(text(subCommand.commandMeta().description(), GRAY))
          .appendNewline();
    }

    message
        .appendNewline()
        .append(text(CC.BAR, GOLD, BOLD))
        .appendSpace()
        .append(text("Page: "))
        .append(text(page, YELLOW, BOLD))
        .append(text("/", DARK_GRAY))
        .append(text(this.totalPages, YELLOW));

    if (page > 1) {
      message
          .appendSpace()
          .append(
              text()
                  .append(text('[', DARK_GRAY))
                  .append(text('«', GOLD))
                  .append(text('«', YELLOW))
                  .append(text(']', DARK_GRAY))
                  .hoverEvent(
                      HoverEvent.showText(text("Cliquez-ici pour passer à la page précédente.")))
                  .clickEvent(
                      ClickEvent.runCommand(
                          "/%s help %d".formatted(this.parentCommand.getName(), page - 1)))
                  .build())
          .appendSpace()
          .appendSpace()
          .appendSpace();
    }

    if (page < this.totalPages) {
      message
          .appendSpace()
          .append(
              text()
                  .append(text('[', DARK_GRAY))
                  .append(text('»', YELLOW))
                  .append(text('»', GOLD))
                  .append(text(']', DARK_GRAY))
                  .hoverEvent(
                      HoverEvent.showText(text("Cliquez-ici pour passer à la page suivante.")))
                  .clickEvent(
                      ClickEvent.runCommand(
                          "/%s help %d".formatted(this.parentCommand.getName(), page + 1)))
                  .build());
    }

    message.appendNewline().append(SEPARATOR);
    audience.sendMessage(message);
  }
}
