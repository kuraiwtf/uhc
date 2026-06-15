package dev.kurai.uhc.command;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

import com.google.common.collect.Lists;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.data.ArgumentData;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class UltraHardcoreOrphanCommand extends Command {

  private final BukkitAudiences bukkitAudiences;
  private final CommandRegistrar commandRegistrar;
  private final Object object;
  private final Method method;
  private final List<ArgumentData> arguments;

  public UltraHardcoreOrphanCommand(
      final CommandMeta meta,
      final BukkitAudiences bukkitAudiences,
      final CommandRegistrar commandRegistrar,
      final Object object,
      final Method method,
      final List<ArgumentData> arguments) {
    super(meta.name(), meta.description(), "/" + meta.name(), Arrays.asList(meta.aliases()));
    this.bukkitAudiences = bukkitAudiences;
    this.commandRegistrar = commandRegistrar;
    this.object = object;
    this.method = method;
    this.arguments = arguments;
  }

  @Override
  public boolean execute(
      final CommandSender sender, final String commandLabel, final String[] args) {
    final var audience = this.bukkitAudiences.sender(sender);
    if (this.method.getParameterTypes()[0].isAssignableFrom(Player.class)
        && !(sender instanceof Player)) {
      audience.sendMessage(
          text()
              .append(prefix())
              .appendSpace()
              .append(text("Seul un joueur peut effectuer cette action.", RED))
              .build());
      return true;
    }

    final int minArgs = this.method.getParameters().length - 1;
    if (args.length < minArgs) {
      audience.sendMessage(
          text()
              .append(prefix())
              .appendSpace()
              .append(text("Vous n'avez pas fourni suffisament d'arguments.", RED))
              .appendSpace()
              .append(text("(", DARK_GRAY))
              .append(text(minArgs, DARK_RED, TextDecoration.BOLD))
              .append(text("/", DARK_GRAY))
              .append(text(args.length, DARK_RED))
              .append(text(")", DARK_GRAY))
              .build());
      return false;
    }

    int arrayParamIndex = -1;
    for (var i = 1; i < this.method.getParameterTypes().length; i++) {
      if (this.method.getParameterTypes()[i].isArray()) {
        arrayParamIndex = i;
        break;
      }
    }

    final var resolvedArguments = Lists.newArrayList();
    resolvedArguments.add(sender);
    final var registrar = this.commandRegistrar.getArgumentResolverRegistrar();

    if (arrayParamIndex < 0) {
      for (var i = 1; i < this.method.getParameters().length; i++) {
        final var resolved =
            registrar.resolveArgument(this.method.getParameterTypes()[i], sender, args[i - 1]);
        if (resolved == null) {
          audience.sendMessage(
              text()
                  .append(prefix())
                  .appendSpace()
                  .append(text("Un argument est invalide.", RED))
                  .build());
          return false;
        }
        resolvedArguments.add(resolved);
      }
    } else {
      final int prefixCount = arrayParamIndex - 1;
      final int suffixCount = this.method.getParameters().length - arrayParamIndex - 1;
      final int arrayLength = args.length - prefixCount - suffixCount;

      for (var i = 1; i < this.method.getParameters().length; i++) {
        final Class<?> type = this.method.getParameterTypes()[i];

        if (i < arrayParamIndex) {
          final var resolved = registrar.resolveArgument(type, sender, args[i - 1]);
          if (resolved == null) {
            audience.sendMessage(
                text()
                    .append(prefix())
                    .appendSpace()
                    .append(text("Un argument est invalide.", RED))
                    .build());
            return false;
          }

          resolvedArguments.add(resolved);
        } else if (i == arrayParamIndex) {
          final Class<?> componentType = type.getComponentType();
          final Object array = Array.newInstance(componentType, arrayLength);
          for (var j = 0; j < arrayLength; j++) {
            final var resolved =
                registrar.resolveArgument(componentType, sender, args[prefixCount + j]);
            if (resolved == null) {
              audience.sendMessage(
                  text()
                      .append(prefix())
                      .appendSpace()
                      .append(text("Un argument est invalide.", RED))
                      .build());
              return false;
            }
            Array.set(array, j, resolved);
          }
          resolvedArguments.add(array);

        } else {
          final int argIndex = args.length - (this.method.getParameters().length - i);
          final var resolved = registrar.resolveArgument(type, sender, args[argIndex]);
          if (resolved == null) {
            audience.sendMessage(
                text()
                    .append(prefix())
                    .appendSpace()
                    .append(text("Un argument est invalide.", RED))
                    .build());
            return false;
          }
          resolvedArguments.add(resolved);
        }
      }
    }

    try {
      this.method.invoke(this.object, resolvedArguments.toArray(new Object[0]));
    } catch (final IllegalAccessException | InvocationTargetException e) {
      audience.sendMessage(
          text()
              .append(prefix())
              .appendSpace()
              .append(text("Une erreur est survenue lors de l'exécution de cette commande.", RED))
              .build());
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public List<String> tabComplete(
      final CommandSender sender, final String alias, final String[] args)
      throws IllegalArgumentException {

    int arrayParamIndex = -1;
    for (var i = 1; i < this.method.getParameterTypes().length; i++) {
      if (this.method.getParameterTypes()[i].isArray()) {
        arrayParamIndex = i;
        break;
      }
    }

    final Class<?> typeToComplete;

    if (arrayParamIndex < 0) {
      if (args.length >= this.method.getParameters().length) {
        return Lists.newArrayList();
      }
      typeToComplete = this.method.getParameterTypes()[args.length];

    } else {
      final int prefixCount = arrayParamIndex - 1;
      final int suffixCount = this.method.getParameters().length - arrayParamIndex - 1;
      final int currentArgIndex = args.length - 1;

      if (currentArgIndex < prefixCount) {
        typeToComplete = this.method.getParameterTypes()[currentArgIndex + 1];

      } else if (suffixCount > 0 && args.length > prefixCount + suffixCount) {
        final int posInSuffix = currentArgIndex - (args.length - suffixCount);
        typeToComplete = this.method.getParameterTypes()[arrayParamIndex + 1 + posInSuffix];
      } else {
        typeToComplete = this.method.getParameterTypes()[arrayParamIndex].getComponentType();
      }
    }

    return Lists.newArrayList(
        this.commandRegistrar
            .getArgumentResolverRegistrar()
            .complete(typeToComplete, sender, args[args.length - 1]));
  }
}
