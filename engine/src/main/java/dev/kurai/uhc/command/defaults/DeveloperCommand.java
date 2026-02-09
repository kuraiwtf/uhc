package dev.kurai.uhc.command.defaults;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.command.annotation.Command;
import dev.kurai.uhc.command.annotation.CommandMeta;
import dev.kurai.uhc.command.argument.annotation.Argument;
import dev.kurai.uhc.debug.chunk.ChunkScanner;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DeveloperCommand {

  private final UltraHardcoreAPI ultraHardcore;

  public DeveloperCommand(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Command(@CommandMeta(name = "chunk-scan"))
  public void chunkScan(final Player player, final @Argument(name = "taille") int size) {
    player.sendMessage("Scanning started!");
    new ChunkScanner(this.ultraHardcore.getWorldService().getWorld(), size)
        .runTaskTimerAsynchronously(this.ultraHardcore.getPlugin(), 0, 1L);
  }
}
