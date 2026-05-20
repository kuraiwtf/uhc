package dev.kurai.uhc.module;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.team.module.TeamModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public final class BuiltinModule extends AbstractModule implements TeamModule {

  private int teamSize;

  private boolean teamView;
  private boolean friendlyFire;
  private boolean randomTeam;

  public BuiltinModule(final @NotNull UltraHardcoreAPI ultraHardcore) {
    super("uhc", "UHC", null, ultraHardcore);
    this.teamSize = 1;
  }

  @Override
  public @NotNull ItemStack provideModuleIcon(@NotNull final Player player) {
    return new ItemStack(Material.GOLDEN_APPLE);
  }
}
