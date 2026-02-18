package dev.kurai.uhc.module;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import net.j4c0b3y.api.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractModule implements Identifiable<String>, Nameable<String> {

  protected final String id;
  protected final String name;

  protected final @Nullable String commandName;

  protected final UltraHardcoreAPI ultraHardcore;

  protected AbstractModule(
      final String id,
      final String name,
      final @Nullable String commandName,
      final UltraHardcoreAPI ultraHardcore) {
    this.id = id;
    this.name = name;
    this.commandName = commandName;
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public final String getId() {
    return this.id;
  }

  @Override
  public final String getName() {
    return this.name;
  }

  public final @Nullable String getCommandName() {
    return this.commandName;
  }

  public final UltraHardcoreAPI getUltraHardcore() {
    return this.ultraHardcore;
  }

  public @Nullable Menu provideModuleMenu(final Player player) {
    return null;
  }

  public abstract ItemStack provideModuleIcon(final Player player);
}
