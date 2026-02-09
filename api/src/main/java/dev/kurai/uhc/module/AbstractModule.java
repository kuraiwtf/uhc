package dev.kurai.uhc.module;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import net.j4c0b3y.api.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractModule
    implements Identifiable<@NotNull String>, Nameable<@NotNull String> {

  protected final String id;
  protected final String name;

  protected final UltraHardcoreAPI ultraHardcore;

  protected AbstractModule(
      final @NotNull String id,
      final @NotNull String name,
      final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.id = id;
    this.name = name;
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public final @NotNull String getId() {
    return this.id;
  }

  @Override
  public final @NotNull String getName() {
    return this.name;
  }

  public final @NotNull UltraHardcoreAPI getUltraHardcore() {
    return this.ultraHardcore;
  }

  public @Nullable Menu provideModuleMenu(final @NotNull Player player) {
    return null;
  }

  public abstract @NotNull ItemStack provideModuleIcon(final @NotNull Player player);
}
