package dev.kurai.uhc.world;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class WorldPreloadTask extends BukkitRunnable {

  private final BukkitAudiences bukkitAudiences;
  private final World world;
  private final int borderSize;

  private int currentX;
  private int currentZ;

  public WorldPreloadTask(
      final @NotNull BukkitAudiences bukkitAudiences, final World world, final int borderSize) {
    this.bukkitAudiences = bukkitAudiences;
    this.world = world;
    this.borderSize = borderSize;

    this.currentX = -borderSize;
    this.currentZ = -borderSize;
  }

  @Override
  public void run() {
    this.world.getBlockAt(this.currentX, 0, this.currentZ).getChunk().load(true);

    //    this.bukkitAudiences
    //        .all()
    //        .sendActionBar(
    //            TRANSLATION_KEY
    //                .append("running")
    //                .withArguments(text(this.currentX), text(this.currentZ)));
    this.currentX += 16;

    if (this.currentX > this.borderSize) {
      this.currentX = -this.borderSize;
      this.currentZ += 16;
    }

    if (this.currentZ > this.borderSize) {
      //      this.bukkitAudiences.all().sendActionBar(TRANSLATION_KEY.append("complete"));
      this.cancel();
    }
  }
}
