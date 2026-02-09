package dev.kurai.uhc.debug.chunk;

import static java.lang.String.format;
import static org.bukkit.Bukkit.broadcastMessage;
import static org.bukkit.Material.*;

import com.google.common.collect.Maps;
import dev.kurai.uhc.util.TimeUtil;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.jspecify.annotations.NullMarked;
import org.spigotmc.AsyncCatcher;

@NullMarked
public final class ChunkScanner extends BukkitRunnable {

  private static final int MINIMAL_Y = 4;
  private static final int MAXIMAL_Y = 63;

  private final World world;
  private final long start;

  private final int borderSize;
  private final int totalToScan;

  private final Map<Material, Integer> results;

  private int currentX;
  private int currentZ;
  private int scannedBlocks;
  private int lastPercentage;

  public ChunkScanner(final World world, final int size) {
    this.world = world;
    this.borderSize = size;
    this.start = System.currentTimeMillis();

    final int horizontalSize = 2 * size + 1;
    final int verticalSize = MAXIMAL_Y - MINIMAL_Y + 1;
    this.totalToScan = horizontalSize * horizontalSize * verticalSize;

    this.results = Maps.newHashMap();

    this.currentX = -size;
    this.currentZ = -size;
    this.lastPercentage = -1;
  }

  @Override
  public void run() {
    if (this.currentX > this.borderSize) {
      this.currentX = -this.borderSize;
      this.currentZ++;
    }

    final double percentage = this.scannedBlocks * 100.0d / this.totalToScan;
    final int currentPercentage = (int) percentage;
    if (currentPercentage > this.lastPercentage) {
      this.lastPercentage = currentPercentage;
      broadcastMessage(
          "Scanned %s%% (%s) (%d/%d)"
              .formatted(
                  format("%.2f", percentage),
                  TimeUtil.formatDuration(System.currentTimeMillis() - this.start),
                  this.scannedBlocks,
                  this.totalToScan));
    }

    if (this.currentZ > this.borderSize) {
      broadcastMessage(
          "Scanning complete! (%s)"
              .formatted(TimeUtil.formatDuration(System.currentTimeMillis() - this.start)));
      for (final var entry :
          this.results.entrySet().stream()
              .sorted(Map.Entry.comparingByValue())
              .toList()
              .reversed()) {
        broadcastMessage(" - %s (x%s)".formatted(entry.getKey(), entry.getValue()));
      }

      broadcastMessage("");
      broadcastMessage("§6§l===== TAUX DE MINERAIS =====");

      final int stoneCount = this.results.getOrDefault(STONE, 0);

      if (stoneCount > 0) {
        final int ironCount = this.results.getOrDefault(IRON_ORE, 0);
        final double ironRatio = (ironCount * 100.0) / stoneCount;
        broadcastMessage(
            "§fFer: §7%d blocs §7(%.3f%% de la pierre)".formatted(ironCount, ironRatio));
        final int goldCount = this.results.getOrDefault(GOLD_ORE, 0);
        final double goldRatio = (goldCount * 100.0) / stoneCount;
        broadcastMessage(
            "§fOr: §e%d blocs §7(%.3f%% de la pierre)".formatted(goldCount, goldRatio));
        final int diamondCount = this.results.getOrDefault(DIAMOND_ORE, 0);
        final double diamondRatio = (diamondCount * 100.0) / stoneCount;
        broadcastMessage(
            "§fDiamant: §b%d blocs §7(%.3f%% de la pierre)".formatted(diamondCount, diamondRatio));
        broadcastMessage("§7(Base: %d blocs de pierre)".formatted(stoneCount));
      } else {
        broadcastMessage("§cAucune pierre trouvée pour calculer les ratios.");
      }

      broadcastMessage("§6§l============================");
      this.cancel();
      return;
    }

    AsyncCatcher.enabled = false;
    try {
      CompletableFuture.runAsync(
          () -> {
            for (int i = 0; i < 16; i++) {
              if (this.currentX > this.borderSize) {
                break;
              }

              for (int y = MINIMAL_Y; y <= MAXIMAL_Y; y++) {
                final var type = this.world.getBlockAt(this.currentX, y, this.currentZ).getType();
                this.results.merge(type, 1, Integer::sum);
                this.scannedBlocks++;
              }

              this.currentX++;
            }
          });
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      AsyncCatcher.enabled = true;
    }
  }
}
