package dev.kurai.uhc.module.builtin;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.module.team.module.TeamModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BuiltinModule extends AbstractModule implements TeamModule {

  public BuiltinModule(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super("uhc", "UHC", ultraHardcore);
  }

  @Override
  public @NotNull ItemStack provideModuleIcon(@NotNull final Player player) {
    return new ItemStack(Material.GOLDEN_APPLE);
  }
}
