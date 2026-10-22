package dev.kurai.uhc.command;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.annotation.DefaultCommand;
import dev.kurai.uhc.command.annotation.SubCommand;
import dev.kurai.uhc.command.argument.ArgumentResolverRegistrar;
import dev.kurai.uhc.command.argument.ArgumentResolverRegistrarImpl;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.command.argument.data.ArgumentData;
import dev.kurai.uhc.command.impl.SubCommandData;
import java.lang.reflect.Field;
import java.util.HashMap;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

public final class CommandRegistrarImpl implements CommandRegistrar {

  private final BukkitAudiences bukkitAudiences;
  private final ArgumentResolverRegistrar argumentResolverRegistrar;
  private PermissionProvider permissionProvider;

  public CommandRegistrarImpl(final UltraHardcoreAPI ultraHardcore) {
    this.bukkitAudiences = ultraHardcore.bukkitAudiences();
    this.argumentResolverRegistrar = new ArgumentResolverRegistrarImpl(this.bukkitAudiences);
    this.permissionProvider = new BuiltinPermissionProvider();
  }

  @Override
  public ArgumentResolverRegistrar getArgumentResolverRegistrar() {
    return this.argumentResolverRegistrar;
  }

  @Override
  public PermissionProvider getPermissionProvider() {
    return this.permissionProvider;
  }

  @Override
  public void setPermissionProvider(final PermissionProvider permissionProvider) {
    this.permissionProvider = permissionProvider;
  }

  @Override
  public void registerCommand(final Object command) {
    this.registerOrphanCommand(command);

    if (command.getClass().isAnnotationPresent(Command.class)) {
      this.registerParentCommand(command);
    }
  }

  private void registerParentCommand(final Object object) {
    this.registerCommand(object.getClass().getAnnotation(Command.class).value(), object);
  }

  @Override
  public void registerCommand(final CommandMeta meta, final Object... handlers) {
    final var subCommands = Lists.<SubCommandData>newArrayList();
    SubCommandData defaultCommand = null;

    for (final var object : handlers) {
      for (final var method : object.getClass().getDeclaredMethods()) {
        final var subCommand = method.getAnnotation(SubCommand.class);
        final var isDefault = method.isAnnotationPresent(DefaultCommand.class);
        if (subCommand == null && !isDefault) {
          continue;
        }

        final var arguments = Lists.<ArgumentData>newArrayList();

        for (var i = 1; i < method.getParameters().length; i++) {
          final var argument = method.getParameters()[i].getAnnotation(Argument.class);
          arguments.add(new ArgumentData(argument.name(), argument.defaultValue()));
        }

        if (isDefault) {
          if (defaultCommand != null) {
            throw new IllegalStateException("Multiple @DefaultCommand for /" + meta.name());
          }
          defaultCommand = new SubCommandData(meta, object, method, arguments);
        }

        if (subCommand != null) {
          subCommands.add(new SubCommandData(subCommand.value(), object, method, arguments));
        }
      }
    }

    this.registerCommand(
        new UltraHardcoreParentCommand(
            meta, this.bukkitAudiences, this, subCommands, defaultCommand));
  }

  private void registerOrphanCommand(final Object object) {
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

  private void registerCommand(final org.bukkit.command.Command command) {
    this.unregisterCommand(command.getName());
    command.getAliases().forEach(this::unregisterCommand);

    this.getCommandMap().register("uhc", command);
  }

  @Override
  public void unregisterCommand(final String name) {
    try {
      final var commandMap = this.getCommandMap();
      final var map = this.getPrivateField(commandMap, "knownCommands");
      @SuppressWarnings("unchecked")
      final HashMap<String, org.bukkit.command.Command> knownCommands =
          (HashMap<String, org.bukkit.command.Command>) map;
      knownCommands.remove(name);
      knownCommands.remove("uhc:" + name);
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

  private SimpleCommandMap getCommandMap() {
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
