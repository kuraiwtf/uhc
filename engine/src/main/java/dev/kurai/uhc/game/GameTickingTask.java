package dev.kurai.uhc.game;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameSecondEvent;
import dev.kurai.uhc.event.defaults.game.GameTickEvent;
import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.RoleComponent;
import dev.kurai.uhc.profile.Profile;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class GameTickingTask implements Runnable {

  private final UltraHardcoreAPI ultraHardcore;

  private int currentTicks;

  @Override
  public void run() {
    Bukkit.getPluginManager().callEvent(new GameTickEvent(this.currentTicks));
    this.currentTicks++;

    for (final Profile profile : this.ultraHardcore.profileService().getPlayingProfiles()) {
      final Player player = profile.getPlayer();
      final RoleComponent roleComponent = profile.getComponent(RoleComponent.class);
      if (player != null && roleComponent != null) {
        final AbstractRole role = roleComponent.role();
        role.onTick(player);
      }
    }

    if (this.currentTicks % 20 == 0) {
      Bukkit.getPluginManager().callEvent(new GameSecondEvent(this.currentTicks / 20));

      final GameService gameService = this.ultraHardcore.gameService();
      gameService.duration(gameService.duration() + 1);

      for (final Profile profile : this.ultraHardcore.profileService().getPlayingProfiles()) {
        final Player player = profile.getPlayer();
        final RoleComponent roleComponent = profile.getComponent(RoleComponent.class);
        if (player != null && roleComponent != null) {
          final AbstractRole role = roleComponent.role();
          role.onSecond(player, gameService.duration());
        }
      }
    }
  }
}
