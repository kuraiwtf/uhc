package dev.kurai.uhc.command;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.help.HelpCommand;
import dev.kurai.uhc.command.impl.SubCommandData;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public final class UltraHardcoreParentCommand extends Command {

  private final BukkitAudiences bukkitAudiences;

  private final CommandRegistrar commandRegistrar;

  private final CommandMeta commandMeta;

  private final List<SubCommandData> subCommands;

  private final @Nullable SubCommandData defaultCommand;

  private final HelpCommand helpCommand;

  public UltraHardcoreParentCommand(
      final CommandMeta meta,
      final BukkitAudiences bukkitAudiences,
      final CommandRegistrar commandRegistrar,
      final List<SubCommandData> subCommands,
      final @Nullable SubCommandData defaultCommand) {
    super(
        meta.name(),
        meta.description(),
        "/" + meta.name() + " help",
        Arrays.asList(meta.aliases()));
    this.bukkitAudiences = bukkitAudiences;

    this.commandRegistrar = commandRegistrar;

    this.commandMeta = meta;

    this.subCommands =
        subCommands.stream()
            .sorted(Comparator.comparing(subCommandData -> subCommandData.commandMeta().name()))
            .toList();

    this.defaultCommand = defaultCommand;

    this.helpCommand = new HelpCommand(this);
  }

  public Collection<SubCommandData> getSubCommands() {
    return Lists.newArrayList(this.subCommands);
  }

  @Override
  public boolean execute(
      final CommandSender sender, final String commandLabel, final String[] args) {
    final PermissionProvider permissionProvider = this.commandRegistrar.getPermissionProvider();
    if (!permissionProvider.hasPermission(sender, this.commandMeta.permission())) {
      sender.sendMessage(prefix("&cVous n'avez pas la permission d'utiliser cette commande."));
      return false;
    }

    final var audience = this.bukkitAudiences.sender(sender);
    if (args.length < 1 && this.defaultCommand != null) {
      return this.executeSubCommand(
          sender, audience, permissionProvider, this.defaultCommand, Lists.newArrayList());
    }

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

    return this.executeSubCommand(sender, audience, permissionProvider, subCommand, rawArguments);
  }

  private boolean executeSubCommand(
      final CommandSender sender,
      final Audience audience,
      final PermissionProvider permissionProvider,
      final SubCommandData subCommand,
      final List<String> rawArguments) {
    final var method = subCommand.method();
    if (!method.getParameterTypes()[0].isInstance(sender)) {
      audience.sendMessage(
          text()
              .append(prefix())
              .append(text("Seul un joueur peut effectuer cette action.", RED))
              .build());
      return true;
    }

    if (!permissionProvider.hasPermission(sender, subCommand.commandMeta().permission())) {
      sender.sendMessage(prefix("&cVous n'avez pas la permission d'utiliser cette commande."));
      return false;
    }

    int arrayParamIndex = -1;
    for (var i = 1; i < method.getParameterTypes().length; i++) {
      if (method.getParameterTypes()[i].isArray()) {
        arrayParamIndex = i;
        break;
      }
    }

    final int minArgs;
    if (arrayParamIndex < 0) {
      int required = 0;
      for (final var argument : subCommand.arguments()) {
        if (argument.defaultValue().isEmpty()) {
          required++;
        }
      }
      minArgs = required;
    } else {
      minArgs = method.getParameters().length - 1;
    }

    if (rawArguments.size() < minArgs) {
      audience.sendMessage(
          text()
              .append(prefix())
              .append(text("Vous n'avez pas fourni suffisament d'arguments.", RED))
              .appendSpace()
              .append(text("(", DARK_GRAY))
              .append(text(rawArguments.size(), DARK_RED, TextDecoration.BOLD))
              .append(text("/", DARK_GRAY))
              .append(text(minArgs, DARK_RED))
              .append(text(")", DARK_GRAY))
              .build());
      return false;
    }

    final var resolvedArguments = Lists.newArrayList();
    resolvedArguments.add(sender);
    final var registrar = this.commandRegistrar.getArgumentResolverRegistrar();

    if (arrayParamIndex < 0) {
      for (var i = 1; i < method.getParameters().length; i++) {
        final String rawArg =
            i - 1 < rawArguments.size()
                ? rawArguments.get(i - 1)
                : subCommand.arguments().get(i - 1).defaultValue();
        final var resolved =
            registrar.resolveArgument(method.getParameterTypes()[i], sender, rawArg);
        if (resolved == null) {
          audience.sendMessage(
              text().append(prefix()).append(text("Un argument est invalide.", RED)).build());
          return false;
        }
        resolvedArguments.add(resolved);
      }
    } else {
      final int prefixCount = arrayParamIndex - 1;
      final int suffixCount = method.getParameters().length - arrayParamIndex - 1;
      final int arrayLength = rawArguments.size() - prefixCount - suffixCount;

      for (var i = 1; i < method.getParameters().length; i++) {
        final Class<?> type = method.getParameterTypes()[i];

        if (i < arrayParamIndex) {
          final var resolved = registrar.resolveArgument(type, sender, rawArguments.get(i - 1));
          if (resolved == null) {
            audience.sendMessage(
                text().append(prefix()).append(text("Un argument est invalide.", RED)).build());
            return false;
          }
          resolvedArguments.add(resolved);

        } else if (i == arrayParamIndex) {
          final Class<?> componentType = type.getComponentType();
          final Object array = Array.newInstance(componentType, arrayLength);
          for (var j = 0; j < arrayLength; j++) {
            final var resolved =
                registrar.resolveArgument(componentType, sender, rawArguments.get(prefixCount + j));
            if (resolved == null) {
              audience.sendMessage(
                  text().append(prefix()).append(text("Un argument est invalide.", RED)).build());
              return false;
            }
            Array.set(array, j, resolved);
          }
          resolvedArguments.add(array);

        } else {
          final int argIndex = rawArguments.size() - (method.getParameters().length - i);
          final var resolved = registrar.resolveArgument(type, sender, rawArguments.get(argIndex));
          if (resolved == null) {
            audience.sendMessage(
                text().append(prefix()).append(text("Un argument est invalide.", RED)).build());
            return false;
          }
          resolvedArguments.add(resolved);
        }
      }
    }

    try {
      method.invoke(subCommand.object(), resolvedArguments.toArray(new Object[0]));
    } catch (final IllegalAccessException | InvocationTargetException e) {
      audience.sendMessage(
          text()
              .append(prefix())
              .append(text("Une erreur est survenue lors de l'exécution de cette commande..", RED))
              .build());
      Bukkit.getLogger()
          .log(Level.SEVERE, "Command /" + this.commandMeta.name() + " failed", e.getCause() != null ? e.getCause() : e);
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
        final String permission = subCommandData.commandMeta().permission();
        if (sender instanceof final Player player
            && !permission.isEmpty()
            && !player.hasPermission(permission)) {
          continue;
        }

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
    }

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

    if (subCommand == null) {
      return defaultCompletions;
    }

    final String permission = subCommand.commandMeta().permission();
    if (!permission.isEmpty()
        && sender instanceof final Player player
        && !player.hasPermission(permission)) {
      return defaultCompletions;
    }

    final var method = subCommand.method();
    if (!method.getParameterTypes()[0].isInstance(sender)) {
      return defaultCompletions;
    }

    final var rawArguments = Lists.newArrayList(args);
    rawArguments.removeFirst();

    int arrayParamIndex = -1;
    for (var i = 1; i < method.getParameterTypes().length; i++) {
      if (method.getParameterTypes()[i].isArray()) {
        arrayParamIndex = i;
        break;
      }
    }

    final Class<?> typeToComplete;

    if (arrayParamIndex < 0) {
      if (rawArguments.size() >= method.getParameters().length) {
        return defaultCompletions;
      }

      typeToComplete = method.getParameterTypes()[rawArguments.size()];
    } else {
      final int prefixCount = arrayParamIndex - 1;
      final int suffixCount = method.getParameters().length - arrayParamIndex - 1;
      final int currentArgIndex = rawArguments.size() - 1;

      if (currentArgIndex < prefixCount) {
        typeToComplete = method.getParameterTypes()[currentArgIndex + 1];
      } else if (suffixCount > 0 && rawArguments.size() > prefixCount + suffixCount) {
        final int posInSuffix = currentArgIndex - (rawArguments.size() - suffixCount);
        typeToComplete = method.getParameterTypes()[arrayParamIndex + 1 + posInSuffix];
      } else {
        typeToComplete = method.getParameterTypes()[arrayParamIndex].getComponentType();
      }
    }

    return Lists.newArrayList(
        this.commandRegistrar
            .getArgumentResolverRegistrar()
            .complete(typeToComplete, sender, rawArguments.getLast()));
  }
}
