package dev.kurai.uhc.util.particle;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;

public final class BlockUtils {

  public static List<Block> getBlocksInRadius(
      final Location location, final int radius, final boolean hollow) {
    final List<Block> blocks = new ArrayList<>();

    final int bX = location.getBlockX();
    final int bY = location.getBlockY();
    final int bZ = location.getBlockZ();
    for (int x = bX - radius; x <= bX + radius; x++) {
      for (int y = bY - radius; y <= bY + radius; y++) {
        for (int z = bZ - radius; z <= bZ + radius; z++) {
          final double distance = (bX - x) * (bX - x) + (bY - y) * (bY - y) + (bZ - z) * (bZ - z);
          if ((distance < radius * radius)
              && ((!hollow) || (distance >= (radius - 1) * (radius - 1)))) {
            final Location l = new Location(location.getWorld(), x, y, z);
            if (l.getBlock().getType() != org.bukkit.Material.BARRIER) {
              blocks.add(l.getBlock());
            }
          }
        }
      }
    }
    return blocks;
  }

  public static List<Block> circle(
      final Location loc,
      final Double r,
      final Double h,
      final Boolean hollow,
      final Boolean sphere,
      final int plus_y) {
    final List<Block> circleblocks = new ArrayList();
    final double cx = loc.getX();
    final double cy = loc.getY();
    final double cz = loc.getZ();

    for (double x = cx - r.doubleValue(); x <= cx + r.doubleValue(); ++x) {
      for (double z = cz - r.doubleValue(); z <= cz + r.doubleValue(); ++z) {
        for (double y = sphere.booleanValue() ? cy - r.doubleValue() : cy;
            y < (sphere.booleanValue() ? cy + r.doubleValue() : cy + h.doubleValue());
            ++y) {
          final double dist =
              (cx - x) * (cx - x)
                  + (cz - z) * (cz - z)
                  + (sphere.booleanValue() ? (cy - y) * (cy - y) : 0.0D);
          if (dist < r.doubleValue() * r.doubleValue()
              && (!hollow.booleanValue()
                  || dist >= (r.doubleValue() - 1.0D) * (r.doubleValue() - 1.0D))) {
            final Location l = new Location(loc.getWorld(), x, y + (double) plus_y, z);
            circleblocks.add(l.getBlock());
          }
        }
      }
    }

    return circleblocks;
  }

  public static List<Location> generateSphere(
      final Location centerBlock, final int radius, final boolean hollow) {
    if (centerBlock == null) {
      return new ArrayList<Location>();
    }
    final List<Location> circleBlocks = new ArrayList<Location>();
    final int bx = centerBlock.getBlockX();
    final int by = centerBlock.getBlockY();
    final int bz = centerBlock.getBlockZ();
    for (int x = bx - radius; x <= bx + radius; ++x) {
      for (int y = by - radius; y <= by + radius; ++y) {
        for (int z = bz - radius; z <= bz + radius; ++z) {
          final double distance = (bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y);
          if (distance < radius * radius && (!hollow || distance >= (radius - 1) * (radius - 1))) {
            final Location l = new Location(centerBlock.getWorld(), x, y, z);
            circleBlocks.add(l);
          }
        }
      }
    }
    return circleBlocks;
  }
}
