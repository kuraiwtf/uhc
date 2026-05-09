package dev.kurai.uhc.command.help;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import dev.kurai.uhc.command.UltraHardcoreParentCommand;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class HelpCommand {

  private static final Component SEPARATOR = CC.line(GOLD, YELLOW);
  private static final int COMMANDS_PER_PAGE = 5;

  private final UltraHardcoreParentCommand parentCommand;
  private final int totalPages;

  public HelpCommand(final @NotNull UltraHardcoreParentCommand parentCommand) {
    this.parentCommand = parentCommand;
    this.totalPages = (parentCommand.getSubCommands().size() / COMMANDS_PER_PAGE) + 1;
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

    for (final var subCommand : this.parentCommand.getSubCommands()) {
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
        .append(text(this.totalPages, YELLOW))
        .appendNewline()
        .append(SEPARATOR);
    audience.sendMessage(message);
  }
}
