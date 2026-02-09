package dev.kurai.uhc.command;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.help.HelpCommand;
import dev.kurai.uhc.command.impl.SubCommandData;
import dev.kurai.uhc.command.registrar.CommandRegistrar;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class UltraHardcoreParentCommand extends Command {

  private final BukkitAudiences bukkitAudiences;
  private final CommandRegistrar commandRegistrar;
  private final List<@NotNull SubCommandData> subCommands;
  private final HelpCommand helpCommand;

  public UltraHardcoreParentCommand(
      final @NotNull CommandMeta meta,
      final @NotNull BukkitAudiences bukkitAudiences,
      final @NotNull CommandRegistrar commandRegistrar,
      final List<@NotNull SubCommandData> subCommands) {
    super(
        meta.name(),
        meta.description(),
        "/" + meta.name() + " help",
        Arrays.asList(meta.aliases()));
    this.bukkitAudiences = bukkitAudiences;
    this.commandRegistrar = commandRegistrar;
    this.subCommands =
        subCommands.stream()
            .sorted(Comparator.comparing(subCommandData -> subCommandData.commandMeta().name()))
            .toList();
    this.helpCommand = new HelpCommand(this);
  }

  public @NotNull Collection<@NotNull SubCommandData> getSubCommands() {
    return Lists.newArrayList(this.subCommands);
  }

  @Override
  public boolean execute(
      final CommandSender sender, final String commandLabel, final String[] args) {
    final var audience = this.bukkitAudiences.sender(sender);
    if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
      final int page;

      if (args.length >= 2) {
        try {
          page = Integer.parseInt(args[1]);
        } catch (final NumberFormatException e) {
          audience.sendMessage(
              text()
                  .append(prefix())
                  .appendSpace()
                  .append(text("La page ", RED))
                  .append(text(args[1], DARK_RED))
                  .append(text(" n'existe pas.", RED))
                  .build());
          return false;
        }
      } else {
        page = 1;
      }

      this.helpCommand.display(audience, page);
      return false;
    }

    final var rawArguments = Lists.newArrayList(args);
    final var subCommandName = rawArguments.removeFirst();
    final var subCommand =
        this.subCommands.stream()
            .filter(
                subCommandData ->
                    subCommandData.commandMeta().name().equalsIgnoreCase(subCommandName)
                        || Arrays.stream(subCommandData.commandMeta().aliases())
                            .anyMatch(s -> s.equalsIgnoreCase(subCommandName)))
            .findFirst()
            .orElse(null);

    if (subCommand == null) {
      audience.sendMessage(
          text()
              .append(prefix())
              .append(text("La sous-commande ", RED))
              .append(text(subCommandName, RED))
              .append(text(" n'existe pas.", RED))
              .build());
      return false;
    }

    final var method = subCommand.method();
    if (method.getParameterTypes()[0].isAssignableFrom(Player.class)
        && !(sender instanceof Player)) {
      audience.sendMessage(
          text()
              .append(prefix())
              .append(text("Seul un joueur peut effectuer cette action.", RED))
              .build());
      return true;
    }

    if (rawArguments.size() < subCommand.arguments().size()) {
      audience.sendMessage(
          text()
              .append(prefix())
              .append(text("Vous n'avez pas fourni suffisament d'arguments.", RED))
              .appendSpace()
              .append(text("(", DARK_GRAY))
              .append(text(rawArguments.size(), DARK_RED, TextDecoration.BOLD))
              .append(text("/", DARK_GRAY))
              .append(text(args.length, DARK_RED))
              .append(text(")", DARK_GRAY))
              .build());
      return false;
    }

    final var resolvedArguments = Lists.newArrayList();
    resolvedArguments.add(sender);

    for (var i = 1; i < method.getParameters().length; i++) {
      final var resolved =
          this.commandRegistrar
              .getArgumentResolverRegistrar()
              .resolveArgument(
                  method.getParameters()[i].getType(), sender, rawArguments.get(i - 1));
      if (resolved == null) {
        audience.sendMessage(
            text().append(prefix()).append(text("Un argument est invalide.", RED)).build());
        return false;
      }

      resolvedArguments.add(resolved);
    }

    try {
      method.invoke(subCommand.object(), resolvedArguments.toArray(new Object[0]));
    } catch (final IllegalAccessException | InvocationTargetException e) {
      audience.sendMessage(
          text()
              .append(prefix())
              .append(text("Une erreur est survenue lors de l'exécution de cette commande..", RED))
              .build());
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public List<String> tabComplete(
      final CommandSender sender, final String alias, final String[] args)
      throws IllegalArgumentException {
    final var defaultCompletions =
        Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();

    if (args.length <= 1) {
      final var completions = Lists.<String>newArrayList();
      final var partial = args.length == 0 ? "" : args[0].toLowerCase();

      for (final var subCommandData : this.subCommands) {
        if (subCommandData.commandMeta().name().toLowerCase().startsWith(partial)) {
          completions.add(subCommandData.commandMeta().name());
        } else if ("help".startsWith(partial)) {
          completions.add("help");
        } else {
          for (final var aliasName : subCommandData.commandMeta().aliases()) {
            if (aliasName.toLowerCase().startsWith(partial)) {
              completions.add(aliasName);
            }
          }
        }
      }

      return completions;
    } else {
      final var subCommandName = args[0];
      final var subCommand =
          this.subCommands.stream()
              .filter(
                  subCommandData ->
                      subCommandData.commandMeta().name().equalsIgnoreCase(subCommandName)
                          || Arrays.stream(subCommandData.commandMeta().aliases())
                              .anyMatch(s -> s.equalsIgnoreCase(subCommandName)))
              .findFirst()
              .orElse(null);

      if (subCommand != null) {
        final var method = subCommand.method();
        if (method.getParameterTypes()[0].isAssignableFrom(Player.class)
            && !(sender instanceof Player)) {
          return defaultCompletions;
        }

        final var rawArguments = Lists.newArrayList(args);
        rawArguments.removeFirst();

        if (rawArguments.size() > subCommand.arguments().size()) {
          return defaultCompletions;
        }

        final var type = method.getParameterTypes()[rawArguments.size()];
        return Lists.newArrayList(
            this.commandRegistrar
                .getArgumentResolverRegistrar()
                .complete(type, sender, !rawArguments.isEmpty() ? rawArguments.getLast() : ""));
      }
    }

    return defaultCompletions;
  }
}
