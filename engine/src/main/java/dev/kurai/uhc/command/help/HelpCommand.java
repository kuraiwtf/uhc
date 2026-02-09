package dev.kurai.uhc.command.help;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.STRIKETHROUGH;

import dev.kurai.uhc.command.UltraHardcoreParentCommand;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class HelpCommand {

  private static final Component BARS = text("-".repeat(7)).decorate(STRIKETHROUGH);
  private static final Component SEPARATOR =
      text()
          .append(BARS.color(DARK_PURPLE))
          .append(BARS.color(LIGHT_PURPLE))
          .append(BARS.append(BARS))
          .append(BARS.color(LIGHT_PURPLE))
          .append(BARS.color(DARK_PURPLE))
          .build();
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
            .append(text("/", DARK_PURPLE))
            .append(text(this.parentCommand.getName(), LIGHT_PURPLE))
            .appendNewline()
            .appendNewline();

    for (final var subCommand : this.parentCommand.getSubCommands()) {
      message
          .appendSpace()
          .append(text(CC.SQUARE, DARK_PURPLE))
          .appendSpace()
          .append(text("/", DARK_PURPLE))
          .append(text(this.parentCommand.getName(), LIGHT_PURPLE))
          .appendSpace()
          .append(text(subCommand.commandMeta().name(), LIGHT_PURPLE))
          .appendSpace()
          .append(text("-", GRAY))
          .appendSpace()
          .append(text(subCommand.commandMeta().description(), GRAY))
          .appendNewline();
    }

    message
        .appendNewline()
        .append(text(CC.BAR, DARK_PURPLE, BOLD))
        .appendSpace()
        .append(text("Page: "))
        .append(text(page, LIGHT_PURPLE, BOLD))
        .append(text("/", DARK_GRAY))
        .append(text(this.totalPages, LIGHT_PURPLE))
        .appendNewline()
        .append(SEPARATOR);
    audience.sendMessage(message);
  }
}
