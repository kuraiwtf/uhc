package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.game.GameService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class DevelopmentCommands {

  private final UltraHardcoreAPI ultraHardcore;

  @Command(@CommandMeta(name = "test"))
  public void test(final Player player) {
    final GameService gameService = this.ultraHardcore.gameService();
    gameService.testMode(!gameService.testMode());
    player.sendMessage("Test mode: " + gameService.testMode());
  }
}
