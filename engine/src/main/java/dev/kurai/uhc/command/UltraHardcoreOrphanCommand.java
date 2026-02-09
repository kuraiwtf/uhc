package dev.kurai.uhc.command;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

import com.google.common.collect.Lists;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.data.ArgumentData;
import dev.kurai.uhc.command.registrar.CommandRegistrar;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class UltraHardcoreOrphanCommand extends Command {

  private final BukkitAudiences bukkitAudiences;
  private final CommandRegistrar commandRegistrar;
  private final Object object;
  private final Method method;
  private final List<@NotNull ArgumentData> arguments;

  public UltraHardcoreOrphanCommand(
      final @NotNull CommandMeta meta,
      final @NotNull BukkitAudiences bukkitAudiences,
      final @NotNull CommandRegistrar commandRegistrar,
      final @NotNull Object object,
      final @NotNull Method method,
      final List<@NotNull ArgumentData> arguments) {
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

    if (args.length < this.arguments.size()) {
      audience.sendMessage(
          text()
              .append(prefix())
              .appendSpace()
              .append(text("Vous n'avez pas fourni suffisament d'arguments.", RED))
              .appendSpace()
              .append(text("(", DARK_GRAY))
              .append(text(this.arguments.size(), DARK_RED, TextDecoration.BOLD))
              .append(text("/", DARK_GRAY))
              .append(text(args.length, DARK_RED))
              .append(text(")", DARK_GRAY))
              .build());
      return false;
    }

    final var resolvedArguments = Lists.newArrayList();
    resolvedArguments.add(sender);

    for (var i = 1; i < this.method.getParameters().length; i++) {
      final var resolved =
          this.commandRegistrar
              .getArgumentResolverRegistrar()
              .resolveArgument(this.method.getParameters()[i].getType(), sender, args[i - 1]);
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

    try {
      this.method.invoke(this.object, resolvedArguments.toArray(new Object[0]));
    } catch (final IllegalAccessException | InvocationTargetException e) {
      audience.sendMessage(
          text()
              .append(prefix())
              .appendSpace()
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
    if (args.length - 1 < this.arguments.size()) {
      final var type = this.method.getParameterTypes()[args.length - 1];
      return Lists.newArrayList(
          this.commandRegistrar
              .getArgumentResolverRegistrar()
              .complete(type, sender, args[args.length - 1]));
    }

    return Lists.newArrayList();
  }
}
