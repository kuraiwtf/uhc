package dev.kurai.uhc.command.registrar;

import static dev.kurai.uhc.command.argument.builtin.JavaArgumentResolvers.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.UltraHardcoreOrphanCommand;
import dev.kurai.uhc.command.UltraHardcoreParentCommand;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.command.argument.data.ArgumentData;
import dev.kurai.uhc.command.argument.registrar.ArgumentResolverRegistrar;
import dev.kurai.uhc.command.argument.registrar.ArgumentResolverRegistrarImpl;
import dev.kurai.uhc.command.impl.SubCommandData;
import java.lang.reflect.Field;
import java.util.HashMap;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

public final class CommandRegistrarImpl implements CommandRegistrar {

  private final BukkitAudiences bukkitAudiences;
  private final UltraHardcoreAPI ultraHardcore;
  private final ArgumentResolverRegistrar argumentResolverRegistrar;

  public CommandRegistrarImpl(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.bukkitAudiences = ultraHardcore.getBukkitAudiences();
    this.ultraHardcore = ultraHardcore;
    this.argumentResolverRegistrar = new ArgumentResolverRegistrarImpl(this.bukkitAudiences);
  }

  @Override
  public @NotNull ArgumentResolverRegistrar getArgumentResolverRegistrar() {
    return this.argumentResolverRegistrar;
  }

  @Override
  public void registerCommand(final @NotNull Object command) {
    this.registerOrphanCommand(command);

    if (command.getClass().isAnnotationPresent(Command.class)) {
      this.registerParentCommand(command);
    }
  }

  private void registerParentCommand(final @NotNull Object object) {
    final var commandAnnotation = object.getClass().getAnnotation(Command.class);
    final var subCommands = Lists.<SubCommandData>newArrayList();

    for (final var method : object.getClass().getDeclaredMethods()) {
      if (!method.isAnnotationPresent(SubCommand.class)) {
        continue;
      }

      final var subCommandMeta = method.getAnnotation(SubCommand.class).value();
      final var arguments = Lists.<ArgumentData>newArrayList();

      for (var i = 1; i < method.getParameters().length; i++) {
        final var argument = method.getParameters()[i].getAnnotation(Argument.class);
        arguments.add(new ArgumentData(argument.name(), argument.defaultValue()));
      }

      subCommands.add(new SubCommandData(subCommandMeta, object, method, arguments));
    }

    this.registerCommand(
        new UltraHardcoreParentCommand(
            commandAnnotation.value(), this.bukkitAudiences, this, subCommands));
  }

  private void registerOrphanCommand(final @NotNull Object object) {
    for (final var method : object.getClass().getDeclaredMethods()) {
      if (!method.isAnnotationPresent(Command.class)) {
        continue;
      }

      final var commandMeta = method.getAnnotation(Command.class).value();
      final var arguments = Lists.<ArgumentData>newArrayList();

      for (var i = 1; i < method.getParameters().length; i++) {
        final var argument = method.getParameters()[i].getAnnotation(Argument.class);
        arguments.add(new ArgumentData(argument.name(), argument.defaultValue()));
      }

      this.registerCommand(
          new UltraHardcoreOrphanCommand(
              commandMeta, this.bukkitAudiences, this, object, method, arguments));
      System.out.println("Registered: " + commandMeta.name());
    }
  }

  private void registerCommand(final @NotNull org.bukkit.command.Command command) {
    this.unregisterCommand(command.getName());
    command.getAliases().forEach(this::unregisterCommand);

    this.getCommandMap().register("uhc", command);
  }

  @Override
  public void unregisterCommand(final @NotNull String name) {
    try {
      final var commandMap = this.getCommandMap();
      final var map = this.getPrivateField(commandMap, "knownCommands");
      @SuppressWarnings("unchecked")
      final HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
      knownCommands.remove(name);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private Object getPrivateField(final Object object, final String field)
      throws SecurityException,
          NoSuchFieldException,
          IllegalArgumentException,
          IllegalAccessException {
    final Class<?> clazz = object.getClass();
    final Field objectField = clazz.getDeclaredField(field);
    objectField.setAccessible(true);
    final Object result = objectField.get(object);
    objectField.setAccessible(false);
    return result;
  }

  private @NotNull SimpleCommandMap getCommandMap() {
    final var server = Bukkit.getServer();
    final var pluginManager = (SimplePluginManager) server.getPluginManager();

    final Field commandMap;
    try {
      commandMap = pluginManager.getClass().getDeclaredField("commandMap");
    } catch (final NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    commandMap.setAccessible(true);
    try {
      return (SimpleCommandMap) commandMap.get(pluginManager);
    } catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
