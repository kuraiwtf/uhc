package dev.kurai.uhc.module;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.team.module.TeamModule;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public final class BuiltinModule extends AbstractModule implements TeamModule {

  private int minTeamSize;
  private int teamSize;
  private int maxTeamSize;

  private boolean teamView;
  private boolean friendlyFire;
  private boolean randomTeam;

  public BuiltinModule(final UltraHardcoreAPI ultraHardcore) {
    super("uhc", "UHC", null, ultraHardcore);
    this.minTeamSize = 1;
    this.teamSize = 1;
    this.maxTeamSize = Integer.MAX_VALUE;
  }

  @Override
  public ItemStack provideModuleIcon(final Player player) {
    return new ItemStack(Material.GOLDEN_APPLE);
  }
}
