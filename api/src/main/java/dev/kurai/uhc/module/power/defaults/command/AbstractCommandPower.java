package dev.kurai.uhc.module.power.defaults.command;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.command.argument.PowerArgument;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class AbstractCommandPower extends AbstractPower {

  protected final String commandName;
  protected PowerArgument @Nullable [] arguments;

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

  public final PowerArgument @Nullable [] getArguments() {
    return this.arguments;
  }

  public final void setArguments(final PowerArgument @Nullable [] arguments) {
    this.arguments = arguments;
  }
}
