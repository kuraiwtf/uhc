package dev.kurai.uhc.game;

import dev.kurai.uhc.event.defaults.game.GameSecondEvent;
import dev.kurai.uhc.event.defaults.game.GameTickEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class GameTickingTask implements Runnable {

  private final GameService gameService;

  private int currentTicks;

  @Override
  public void run() {
    Bukkit.getPluginManager().callEvent(new GameTickEvent(this.currentTicks));
    this.currentTicks++;

    if (this.currentTicks % 20 == 0) {
      Bukkit.getPluginManager().callEvent(new GameSecondEvent(this.currentTicks / 20));
      this.gameService.duration(this.gameService.duration() + 1);
    }
  }
}
