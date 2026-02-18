package dev.kurai.uhc.module.power.defaults.command;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import java.util.UUID;

import dev.kurai.uhc.module.power.defaults.command.argument.PowerArgument;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractCommandPower extends AbstractPower {

  protected final String commandName;
  protected PowerArgument[] arguments;

  public AbstractCommandPower(
      final String identifier,
      final String name,
      final UUID owner,
      final UltraHardcoreAPI ultraHardcore,
      final String commandName) {
    super(identifier, name, owner, ultraHardcore);
    this.commandName = commandName;
  }

  public final String getCommandName() {
    return this.commandName;
  }

  public final PowerArgument @NotNull [] getArguments() {
    return this.arguments;
  }

  public final void setArguments(final PowerArgument @NotNull [] arguments) {
    this.arguments = arguments;
  }
}
