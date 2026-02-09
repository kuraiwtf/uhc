package dev.kurai.uhc.util.particle;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class MathUtil {

  public static void sendParticle(final EnumParticle particle, final Location location) {
    sendParticle(particle, location.getX(), location.getY(), location.getZ());
  }

  public static void sendParticle(
      final @NotNull Player player, final EnumParticle particle, final Location location) {
    sendParticle(player, particle, location.getX(), location.getY(), location.getZ());
  }

  public static void sendParticle(
      final EnumParticle particle, final double x, final double y, final double z) {
    for (final Player p : Bukkit.getOnlinePlayers()) {
      sendParticle(p, particle, x, y, z);
    }
  }

  public static void sendParticle(
      final @NotNull Player player,
      final EnumParticle particle,
      final double x,
      final double y,
      final double z) {
    final PacketPlayOutWorldParticles packet =
        new PacketPlayOutWorldParticles(
            particle,
            true,
            (float) x,
            (float) y,
            (float) z,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            10,
            (int[]) null);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }

  public static void sendCircleParticle(
      final EnumParticle particle, final Location center, final double radius, final int amount) {
    final double increment = 6.283185307179586 / amount;
    for (int i = 0; i < amount; ++i) {
      final double angle = i * increment;
      final double x = center.getX() + radius * Math.cos(angle);
      final double z = center.getZ() + radius * Math.sin(angle);
      sendParticle(particle, x, center.getY() + 0.5, z);
    }
  }

  public static List<Location> getSphere(
      final Location centerBlock, final int radius, final boolean hollow) {
    final List<Location> circleBlocks = new ArrayList<>();
    final int bX = centerBlock.getBlockX();
    final int bY = centerBlock.getBlockY();
    final int bZ = centerBlock.getBlockZ();
    for (int x = bX - radius; x <= bX + radius; ++x) {
      for (int y = bY - radius; y <= bY + radius; ++y) {
        for (int z = bZ - radius; z <= bZ + radius; ++z) {
          final double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
          if (distance < radius * radius && (!hollow || distance >= (radius - 1) * (radius - 1))) {
            circleBlocks.add(new Location(centerBlock.getWorld(), x, y, z));
          }
        }
      }
    }
    return circleBlocks;
  }
}
