package dev.kurai.uhc.util.particle;

import dev.kurai.uhc.util.GlobalUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Vector3f;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class ParticleUtil {

  public static void sendCircleParticle(
      final int r,
      final int g,
      final int b,
      final Location center,
      final double radius,
      final int amount) {
    final double increment = 6.283185307179586 / amount;
    for (int i = 0; i < amount; ++i) {
      final double angle = i * increment;
      final double x = center.getX() + radius * Math.cos(angle);
      final double z = center.getZ() + radius * Math.sin(angle);
      final Location particleLocation = new Location(center.getWorld(), x, center.getY() + 0.5, z);
      spawnParticle(particleLocation, r, g, b);
    }
  }

  public static void sendCircleParticleThunder(
      final int r,
      final int g,
      final int b,
      final Location center,
      final double radius,
      final int amount) {
    final double increment = 6.283185307179586 / amount;
    for (int i = 0; i < amount; ++i) {
      final double angle = i * increment;
      final double x = center.getX() + radius * Math.cos(angle);
      final double z = center.getZ() + radius * Math.sin(angle);
      final Location particleLocation = new Location(center.getWorld(), x, center.getY() + 0.5, z);
      createLightningEffect(particleLocation, 4, 5, r, g, b);
    }
  }

  public static void sendPentagramParticle(final Location l, final EnumParticle p, final float r) {
    final double angleIncrement = 2 * Math.PI / 5;
    for (double t = 0; t < 2 * Math.PI; t += angleIncrement) {
      final float x1 = r * (float) Math.sin(t);
      final float z1 = r * (float) Math.cos(t);
      final float x2 = r * (float) Math.sin(t + angleIncrement);
      final float z2 = r * (float) Math.cos(t + angleIncrement);
      final Location point1 = l.clone().add(x1, 0, z1);
      final Location point2 = l.clone().add(x2, 0, z2);
      spawnParticleLine(point1, point2, p, 10);
      spawnParticleLine(point1, l, p, 10);
    }
  }

  public static void sendPentagramParticle(
      final Location location, final java.awt.Color color, final float radius) {
    final double angleIncrement = 2 * Math.PI / 5;
    for (double t = 0; t < 2 * Math.PI; t += angleIncrement) {
      final float x1 = radius * (float) Math.sin(t);
      final float z1 = radius * (float) Math.cos(t);
      final float x2 = radius * (float) Math.sin(t + angleIncrement);
      final float z2 = radius * (float) Math.cos(t + angleIncrement);
      final Location point1 = location.clone().add(x1, 0, z1);
      final Location point2 = location.clone().add(x2, 0, z2);
      spawnColoredLine(point1, point2, 10, color.getRed(), color.getGreen(), color.getBlue());
      spawnColoredLine(point1, location, 10, color.getRed(), color.getGreen(), color.getBlue());
    }
  }

  public static ItemStack createIronSwordItem() {
    final ItemStack swordItem =
        new ItemStack(net.minecraft.server.v1_8_R3.Item.getById(Material.IRON_SWORD.getId()));

    final NBTTagCompound tag = swordItem.hasTag() ? swordItem.getTag() : new NBTTagCompound();
    tag.set("swordOfParticles", new NBTTagInt(1));
    swordItem.setTag(tag);

    return swordItem;
  }

  public static void drawSword(final Location loc) {
    for (double y = 0; y <= 1; y += 0.1) {
      spawnParticle(loc.clone().add(0, y, 0), 253, 108, 158);
    }
    for (double y = 1; y <= 1.5; y += 0.1) {
      spawnParticle(loc.clone().add(0, y, 0), 253, 108, 158);
    }
    spawnParticle(loc.clone().add(0, 1.5, 0), 253, 108, 158);
  }

  public static void createParticleArmor(final Player player) {
    final Location loc = player.getLocation();
    loc.setY(loc.getY() - 0.16);

    drawHelmet(loc.clone().add(0, 2.0, 0), 253, 108, 158, player);

    drawChestplate(loc.clone().add(0, 1.5, 0), 253, 108, 158, player);

    drawLeggings(loc.clone().add(0, 1.0, 0), 253, 108, 158, player);

    drawBoots(loc.clone().add(0, 0.5, 0), 253, 108, 158, player);
  }

  public static void drawHelmet(
      final Location loc, final int red, final int green, final int blue, final Player player) {
    final double radius = 0.35;
    final int points = 20;
    for (int i = 0; i < points; i++) {
      final double angle = 2 * Math.PI * i / points;
      final double x = radius * Math.cos(angle);
      final double z = radius * Math.sin(angle);
      spawnParticle(loc.clone().add(x, 0.3, z), red, green, blue, player);
      spawnParticle(loc.clone().add(x, -0.3, z), red, green, blue, player);
    }
  }

  public static void drawChestplate(
      final Location loc, final int red, final int green, final int blue, final Player player) {
    final double width = 0.4;
    final double height = 0.6;
    for (double y = -height / 2; y <= height / 2; y += 0.1) {
      spawnParticle(loc.clone().add(width, y, 0), red, green, blue, player);
      spawnParticle(loc.clone().add(-width, y, 0), red, green, blue, player);
      spawnParticle(loc.clone().add(0, y, width), red, green, blue, player);
      spawnParticle(loc.clone().add(0, y, -width), red, green, blue, player);
    }
  }

  public static void drawLeggings(
      final Location loc, final int red, final int green, final int blue, final Player player) {
    final double width = 0.35;
    final double height = 0.5;
    for (double y = -height / 2; y <= height / 2; y += 0.1) {
      spawnParticle(loc.clone().add(width, y, 0), red, green, blue, player);
      spawnParticle(loc.clone().add(-width, y, 0), red, green, blue, player);
      spawnParticle(loc.clone().add(0, y, width), red, green, blue, player);
      spawnParticle(loc.clone().add(0, y, -width), red, green, blue, player);
    }
  }

  public static void drawBoots(
      final Location loc, final int red, final int green, final int blue, final Player player) {
    final double width = 0.3;
    final double height = 0.2;
    for (double y = -height / 2; y <= height / 2; y += 0.1) {
      spawnParticle(loc.clone().add(width, y, 0), red, green, blue);
      spawnParticle(loc.clone().add(-width, y, 0), red, green, blue);
      spawnParticle(loc.clone().add(0, y, width), red, green, blue);
      spawnParticle(loc.clone().add(0, y, -width), red, green, blue);
    }
  }

  public static void sendCircleParticle(final Location l, final EnumParticle p, final float r) {
    for (double t = 0; t < 50; t += 0.5) {
      final float x = r * (float) Math.sin(t);
      final float z = r * (float) Math.cos(t);
      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              p, true, (float) l.getX() + x, (float) l.getY(), (float) l.getZ() + z, 0, 0, 0, 0, 1);
      for (final Player pls : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public static void sendCirclePerfectParticle(
      final Location l, final int red, final int g, final int b, final float r) {
    for (double t = 0; t < 50; t += 0.5) {
      final float x = r * (float) Math.sin(t);
      final float z = r * (float) Math.cos(t);
      final Location particle = l.clone();
      particle.setX(l.getX() + x);
      particle.setZ(l.getZ() + z);
      spawnParticle(particle, red, g, b);
    }
  }

  public static void sendCircleSpellPerfectParticle(
      final Location l, final int red, final int g, final int b, final float r) {
    for (double t = 0; t < 50; t += 0.5) {
      final float x = r * (float) Math.sin(t);
      final float z = r * (float) Math.cos(t);
      final Location particle = l.clone();
      particle.setX(l.getX() + x);
      particle.setZ(l.getZ() + z);
      spawnParticle(particle, red, g, b);
    }
  }

  public static void sendCircleParticle2(final Location l, final EnumParticle p, final float r) {
    for (double t = 0; t < 50; t += 0.5) {
      final float x = r * (float) Math.sin(t);
      final float z = r * (float) Math.cos(t);
      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              p,
              true,
              (float) l.getX() + x,
              (float) l.getY() + 1,
              (float) l.getZ() + z,
              0,
              0,
              0,
              0,
              1);
      for (final Player pls : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public static void sendVerticalCircleParticle(
      final Location loc, final EnumParticle particle, final float radius) {
    for (double t = 0; t < 50; t += 0.5) {
      final float x = radius * (float) Math.cos(t);
      final float y = radius * (float) Math.sin(t);
      final float yaw = loc.getYaw();
      final double rad = Math.toRadians(yaw);

      final float dx = x * (float) Math.cos(rad) - y * (float) Math.sin(rad);
      final float dz = x * (float) Math.sin(rad) + y * (float) Math.cos(rad);

      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              particle,
              true,
              (float) loc.getX() + dx,
              (float) loc.getY() + y,
              (float) loc.getZ() + dz,
              0,
              0,
              0,
              0,
              1);
      for (final Player player : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public static void createHelix(final Player player, final EnumParticle p) {
    final Location loc = player.getLocation();
    final int radius = 2;
    double y;
    for (y = 0.0D; y <= 50.0D; y += 0.05D) {
      final double x = radius * Math.cos(y);
      final double z = radius * Math.sin(y);
      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              p,
              true,
              (float) (loc.getX() + x),
              (float) (loc.getY() + y),
              (float) (loc.getZ() + z),
              0.0F,
              0.0F,
              0.0F,
              0.0F,
              1);
      for (final Player online : Bukkit.getOnlinePlayers()) {
        (((CraftPlayer) online).getHandle()).playerConnection.sendPacket(packet);
      }
    }
  }

  public static void createHorizontalHelix(
      final Location pointA,
      final Location pointB,
      final double radius,
      final EnumParticle particle) {
    final Vector direction = pointB.toVector().subtract(pointA.toVector());
    final double length = direction.length();
    direction.normalize();

    final double particlesPerRevolution = 20;
    final double revolutions = length / radius;
    final double stepSize = Math.PI * 2 / particlesPerRevolution;

    for (double t = 0; t < length; t += stepSize) {
      final Vector currentPoint = pointA.toVector().clone().add(direction.clone().multiply(t));
      final double offsetX = radius * Math.cos(t * revolutions);
      final double offsetZ = radius * Math.sin(t * revolutions);

      final Location particleLocation =
          new Location(
              pointA.getWorld(),
              currentPoint.getX() + offsetX,
              currentPoint.getY(),
              currentPoint.getZ() + offsetZ);

      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              particle,
              true,
              (float) particleLocation.getX(),
              (float) particleLocation.getY(),
              (float) particleLocation.getZ(),
              0.0F,
              0.0F,
              0.0F,
              0.0F,
              1);

      for (final Player online : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public static void createHelixBetweenPoints(
      final Player player, final Location startLocation, final Location endLocation) {
    final int points = 100;
    final double radius = 2;
    double y = 0;

    final double deltaX = (endLocation.getX() - startLocation.getX()) / points;
    final double deltaY = (endLocation.getY() - startLocation.getY()) / points;
    final double deltaZ = (endLocation.getZ() - startLocation.getZ()) / points;

    for (int i = 0; i < points; i++) {
      final double x = radius * Math.cos(y);
      final double z = radius * Math.sin(y);

      final Location particleLocation =
          new Location(
              startLocation.getWorld(),
              startLocation.getX() + deltaX * i + x,
              startLocation.getY() + deltaY * i + y,
              startLocation.getZ() + deltaZ * i + z);

      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              EnumParticle.NOTE,
              true,
              (float) particleLocation.getX(),
              (float) particleLocation.getY(),
              (float) particleLocation.getZ(),
              0.0F,
              0.0F,
              0.0F,
              0.0F,
              1);

      for (final Player online : Bukkit.getOnlinePlayers()) {
        (((CraftPlayer) online).getHandle()).playerConnection.sendPacket(packet);
      }

      y += 0.05;
    }
  }

  public static void spawnWave(
      final Player p, final Location l, final Double range, final int r, final int g, final int b) {
    final Location playerLocation = p.getLocation();
    final Vector direction = playerLocation.getDirection().normalize();
    final double yaw = Math.atan2(direction.getZ(), direction.getX());
    final double radius = range;
    final double numParticles = (radius * 25);

    for (double t = -Math.PI / 4; t <= Math.PI / 4; t += Math.PI / (numParticles / 2)) {
      final double x = radius * Math.cos(t);
      final double z = radius * Math.sin(t);
      final double rotatedX = x * Math.cos(yaw) - z * Math.sin(yaw);
      final double rotatedZ = x * Math.sin(yaw) + z * Math.cos(yaw);
      final Location particleLocation = l.clone().add(rotatedX, 0, rotatedZ);
      spawnParticle(particleLocation, r, g, b);
    }
  }

  public static void spawnParticleCape(final Player player) {
    final Location loc = player.getLocation();
    final double radius = 0.3;
    final double height = 1.8;
    final int particleCount = 5;

    for (double y = 0; y <= height; y += 0.1) {
      for (double t = 0; t < 2 * Math.PI; t += Math.PI / particleCount) {
        final double x = radius * Math.cos(t);
        final double z = radius * Math.sin(t);

        final Vector direction = player.getLocation().getDirection().normalize();
        final Location particleLocation =
            loc.clone().add(-direction.getZ() * 0.5, y, direction.getX() * 0.5);
        particleLocation.add(x, 0, z);

        spawnParticle(particleLocation, 2, 3, 2);
      }
    }
  }

  public static void vagueSpellWave(
      final Player p, final Location l, final Double range, final int r, final int g, final int b) {
    final Location playerLocation = p.getLocation();
    final Vector direction = playerLocation.getDirection().normalize();
    final double yaw = Math.atan2(direction.getZ(), direction.getX());
    final double radius = range;
    final double numParticles = (radius * 25);

    for (double t = -Math.PI / 4; t <= Math.PI / 4; t += Math.PI / (numParticles / 2)) {
      final double x = radius * Math.cos(t);
      final double z = radius * Math.sin(t);
      final double rotatedX = x * Math.cos(yaw) - z * Math.sin(yaw);
      final double rotatedZ = x * Math.sin(yaw) + z * Math.cos(yaw);
      final Location particleLocation = l.clone().add(rotatedX, 0, rotatedZ);
      spawnSpellParticle(particleLocation, r, g, b);
    }
  }

  public static void createHelix3(final Player player) {
    final Location loc = player.getLocation();
    final int radius = 2;
    double y;
    for (y = 0.0D; y <= 50.0D; y += 0.05D) {
      final double x = radius * Math.cos(y);
      final double z = radius * Math.sin(y);
      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              EnumParticle.HEART,
              true,
              (float) (loc.getX() + x),
              (float) (loc.getY() + y),
              (float) (loc.getZ() + z),
              0.0F,
              0.0F,
              0.0F,
              0.0F,
              1);
      for (final Player online : Bukkit.getOnlinePlayers()) {
        (((CraftPlayer) online).getHandle()).playerConnection.sendPacket(packet);
      }
    }
  }

  public static void createHelix2(final Player player) {
    final Location loc = player.getLocation();
    final int radius = 2;
    double y;
    for (y = 0.0D; y <= 50.0D; y += 0.05D) {
      final double x = radius * Math.cos(y);
      final double z = radius * Math.sin(y);
      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              EnumParticle.FLAME,
              true,
              (float) (loc.getX() + x),
              (float) (loc.getY() + y),
              (float) (loc.getZ() + z),
              0.0F,
              0.0F,
              0.0F,
              0.0F,
              1);
      for (final Player online : Bukkit.getOnlinePlayers()) {
        (((CraftPlayer) online).getHandle()).playerConnection.sendPacket(packet);
      }
    }
  }

  public static void playShurikenEffect(final Location location) {
    final double[] radii = {1.3, 1.1, 0.9};
    final int points = 3;
    final double angleIncrement = Math.PI * 2 / points;
    final double[] angles = {0, Math.PI * 2 / 3, Math.PI * 4 / 3};

    for (final double radius : radii) {
      for (final double angleOffset : angles) {
        for (int i = 0; i < points; i++) {
          final double angle = i * angleIncrement + angleOffset;
          final double x = location.getX() + radius * Math.cos(angle);
          final double z = location.getZ() + radius * Math.sin(angle);
          final Location particleLocation =
              new Location(location.getWorld(), x, location.getY(), z);
          MathUtil.sendParticle(EnumParticle.DRIP_WATER, particleLocation);
        }
      }
    }
  }

  public static void playGiantShurikenEffect(
      final Location center, final double distance, final int r, final int g, final int b) {
    for (int j = 0; j < 3; j++) {
      final List<Location> points = new ArrayList<>();
      final int pointsCount = 3;
      final double angleIncrement = Math.PI * 2 / pointsCount;
      final double angleOffset = new Random().nextDouble() * Math.PI * 2;

      for (int i = 0; i < pointsCount; i++) {
        final double angle = i * angleIncrement + angleOffset;
        final double x = center.getX() + distance * Math.cos(angle);
        final double z = center.getZ() + distance * Math.sin(angle);
        points.add(new Location(center.getWorld(), x, center.getY(), z));
      }
      for (int i = 0; i < points.size(); i++) {
        final Location from = points.get(i);
        final Location to = points.get((i + 1) % points.size());
        spawnPerfectColoredParticleLine(from, to, r, g, b, 10);
      }
    }
  }

  public static void sendColoredCircleParticle(
      final Location l, final EnumParticle p, final float r, final String color) {
    final String[] colorValues = color.split("#");
    if (colorValues.length != 2 || colorValues[1].length() != 6) {
      throw new IllegalArgumentException("Invalid color format. Please use #RRGGBB.");
    }
    final int red = Integer.parseInt(colorValues[1].substring(0, 2), 16);
    final int green = Integer.parseInt(colorValues[1].substring(2, 4), 16);
    final int blue = Integer.parseInt(colorValues[1].substring(4, 6), 16);

    for (double t = 0; t < 50; t += 0.5) {
      final float x = r * (float) Math.sin(t);
      final float z = r * (float) Math.cos(t);

      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              p,
              true,
              (float) l.getX() + x,
              (float) l.getY(),
              (float) l.getZ() + z,
              (float) red / 255,
              (float) green / 255,
              (float) blue / 255,
              0,
              1);

      for (final Player player : Bukkit.getOnlinePlayers()) {

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public static void sendBlueRedstoneWaveParticle(final Player player, final double radius) {
    final Location playerLocation = player.getLocation();

    final double playerX = playerLocation.getX();
    final double playerY = playerLocation.getY();
    final double playerZ = playerLocation.getZ();

    for (double t = 0; t < 360; t += 10) {
      final double angleInRadians = Math.toRadians(t);
      final double xOffset = radius * Math.cos(angleInRadians);
      final double zOffset = radius * Math.sin(angleInRadians);

      final double particleX = playerX + xOffset;
      final double particleY = playerY;
      final double particleZ = playerZ + zOffset;

      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(
              EnumParticle.REDSTONE, // Type de particule
              true, // Long distance
              (float) particleX, // X
              (float) particleY, // Y
              (float) particleZ, // Z
              0.0f, // Offset X
              0.0f, // Offset Y
              0.0f, // Offset Z
              2.0f,
              3);
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
  }

  public static void spawnPerfectColoredParticleLine(
      final Location from,
      final Location to,
      final int r,
      final int g,
      final int b,
      final int count) {
    final double distance = from.distance(to);
    final Vector direction = to.toVector().subtract(from.toVector()).normalize();
    final double spacing = distance / count;
    for (int i = 0; i < count; i++) {
      final Location particleLocation = from.clone().add(direction.clone().multiply(spacing * i));
      spawnParticle(particleLocation, r, g, b);
    }
  }

  public static void spawnColoredSusano(
      final Player player, final int r, final int g, final int b, final int count) {
    final Location eyeLocation = player.getEyeLocation();
    final Location playerLocation = player.getLocation();
    final Vector backwardDirection = eyeLocation.getDirection().multiply(-0.14).normalize();
    final double yBottom = playerLocation.getY() - 0.1;
    final double yTop = playerLocation.getY() + 1.9;
    eyeLocation.add(backwardDirection);
    eyeLocation.setY(yTop);
    playerLocation.add(backwardDirection);
    playerLocation.setY(yBottom);
    spawnPerfectColoredParticleLine(eyeLocation, playerLocation, r, g, b, count);

    final double radius = 0.9;
    final double playerYaw = Math.toRadians(player.getLocation().getYaw());
    final double startAngle = playerYaw + Math.toRadians(50);
    double endAngle = playerYaw + Math.toRadians(310);
    if (endAngle > 2 * Math.PI) {
      endAngle -= 2 * Math.PI;
    }

    final double angleStep = (endAngle - startAngle) / (count - 1);
    for (int j = 0; j < 3; j++) {
      for (int i = 0; i < count; i++) {
        final double angle = startAngle + i * angleStep;
        final float x = (float) (radius * Math.sin(angle));
        final float z = (float) (radius * Math.cos(angle));
        final Location particleLocation = playerLocation.clone();
        particleLocation.setX(playerLocation.getX() + x);
        particleLocation.setZ(playerLocation.getZ() + z);
        spawnParticle(particleLocation, r, g, b);
      }
      playerLocation.setY(playerLocation.getY() + 0.95);
    }
  }

  public static void createTornado(
      final Location centerLocation,
      final int numParticles,
      final double radius,
      final double height,
      final EnumParticle particle) {
    for (double angle = 0; angle < 2 * Math.PI; angle += (2 * Math.PI) / numParticles) {
      final double x = radius * Math.cos(angle);
      final double z = radius * Math.sin(angle);
      for (double y = 0; y < height; y += 0.1) {
        final double xOffset = Math.sin(y) * x;
        final double zOffset = Math.sin(y) * z;
        final Location particleLocation = centerLocation.clone().add(xOffset, y, zOffset);
        final PacketPlayOutWorldParticles packet =
            new PacketPlayOutWorldParticles(
                particle,
                true,
                (float) particleLocation.getX(),
                (float) particleLocation.getY(),
                (float) particleLocation.getZ(),
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                4);
        for (final Player player : particleLocation.getWorld().getPlayers()) {
          ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
      }
    }
  }

  public static void createSafeTornado(
      final Location centerLocation,
      final int numParticles,
      final double radius,
      final double height,
      final EnumParticle particle) {

    for (double y = height; y > 0; y -= 0.1) {
      final double angleStep = (2 * Math.PI) / numParticles;
      final double currentRadius = radius * (y / height);

      for (double angle = 0; angle < 2 * Math.PI; angle += angleStep) {
        final double xOffset = currentRadius * Math.cos(angle + (y * 0.1));
        final double zOffset = currentRadius * Math.sin(angle + (y * 0.1));
        final Location particleLocation = centerLocation.clone().add(xOffset, y, zOffset);

        final PacketPlayOutWorldParticles packet =
            new PacketPlayOutWorldParticles(
                particle,
                true,
                (float) particleLocation.getX(),
                (float) particleLocation.getY(),
                (float) particleLocation.getZ(),
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1);
        for (final Player player : centerLocation.getWorld().getPlayers()) {
          ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
      }
    }
  }

  public static void sendLineParticle(final Location l, final EnumParticle p) {
    float x = (float) l.getX();
    final float y = (float) l.getY();
    final float z = (float) l.getZ();
    for (int t = 0; t < 100; t++) {
      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(p, true, x, y, z, 0, 0, 0, 0, 1);
      for (final Player pls : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
      }
      x = +0.1f;
    }
  }

  public static void sendLineParticleL(
      final Location pointA, final Location pointB, final EnumParticle p) {
    final int particleCount = 100;
    final double stepSize = 1.0 / particleCount;

    final Vector vector = pointB.toVector().subtract(pointA.toVector());

    for (int t = 0; t < particleCount; t++) {
      final double progress = t * stepSize;
      final Location particleLocation = pointA.clone().add(vector.clone().multiply(progress));

      final float x = (float) particleLocation.getX();
      final float y = (float) particleLocation.getY();
      final float z = (float) particleLocation.getZ();

      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(p, true, x, y, z, 0, 0, 0, 0, 1);
      for (final Player pls : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public static List<Location> generateLightningPoints(
      final Location startLocation, final int pointsCount) {
    final List<Location> points = new ArrayList<>();
    final Random random = new Random();
    final Location currentLocation = startLocation.clone();
    for (int i = 0; i < pointsCount; i++) {
      points.add(currentLocation.clone());
      final double offsetX = (random.nextDouble() - 0.5) * 2;
      final double offsetY = 1.0;
      final double offsetZ = (random.nextDouble() - 0.5) * 2;
      currentLocation.add(offsetX, offsetY, offsetZ);
    }
    return points;
  }

  public static void sendLight(
      final Location pointA, final Location pointB, final EnumParticle particle) {
    final int particleCount = 100;
    final double stepSize = 1.0 / particleCount;

    final Vector vector = pointB.toVector().subtract(pointA.toVector());

    for (int t = 0; t < particleCount; t++) {
      final double progress = t * stepSize;
      final Location particleLocation = pointA.clone().add(vector.clone().multiply(progress));

      final float x = (float) particleLocation.getX();
      final float y = (float) particleLocation.getY();
      final float z = (float) particleLocation.getZ();

      final PacketPlayOutWorldParticles packet =
          new PacketPlayOutWorldParticles(particle, true, x, y, z, 0, 0, 0, 0, 1);
      for (final Player player : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public static void createLightningEffect(
      final Location startLocation,
      final int pointsCount,
      final int particleCount,
      final int r,
      final int g,
      final int b) {
    final List<Location> points = new ArrayList<>();
    final Random random = new Random();
    final Location currentLocation = startLocation.clone();
    for (int i = 0; i < pointsCount; i++) {
      points.add(currentLocation.clone());
      final double offsetX = (random.nextDouble() - 0.5) * 2;
      final double offsetY = 1.0;
      final double offsetZ = (random.nextDouble() - 0.5) * 2;
      currentLocation.add(offsetX, offsetY, offsetZ);
    }
    for (int i = 0; i < points.size() - 1; i++) {
      final double stepSize = 1.0 / particleCount;

      final Vector vector = points.get(i + 1).toVector().subtract(points.get(i).toVector());

      for (int t = 0; t < particleCount; t++) {
        final double progress = t * stepSize;
        final Location particleLocation =
            points.get(i).clone().add(vector.clone().multiply(progress));
        spawnSpellParticle(particleLocation, r, g, b);
      }
    }
  }

  public static void sendSphereParticle(
      final Location location, final EnumParticle p, final double r) {
    final double phiStep = Math.PI / 10;
    final double thetaStep = Math.PI / 40;

    final float locX = (float) location.getX();
    final float locY = (float) location.getY();
    final float locZ = (float) location.getZ();

    for (double phi = 0; phi <= Math.PI; phi += phiStep) {
      final double sinPhi = Math.sin(phi);
      final double cosPhi = Math.cos(phi);

      for (double theta = 0; theta <= 2 * Math.PI; theta += thetaStep) {
        final double cosTheta = Math.cos(theta);
        final double sinTheta = Math.sin(theta);

        final float x = (float) (r * cosTheta * sinPhi);
        final float y = (float) (r * cosPhi);
        final float z = (float) (r * sinTheta * sinPhi);

        final float particleX = locX + x;
        final float particleY = locY + y;
        final float particleZ = locZ + z;

        final PacketPlayOutWorldParticles packet =
            new PacketPlayOutWorldParticles(
                p, true, particleX, particleY, particleZ, 0, 0, 0, 0, 1);

        for (final Player player : Bukkit.getOnlinePlayers()) {
          ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
      }
    }
  }

  public static void playDissEffect(
      final Player player, final EnumParticle p, final Location location) {
    final Location playerLocation = location;

    final double crossDistance = 2.0;

    final int particleCount = 50;

    final double angleIncrement = 2 * Math.PI / particleCount;

    for (double angle = 0; angle < 2 * Math.PI; angle += angleIncrement) {
      final double x = crossDistance * Math.cos(angle);
      final double z = crossDistance * Math.sin(angle);
      MathUtil.sendParticle(
          p, playerLocation.getX() + x, playerLocation.getY(), playerLocation.getZ() + z);

      MathUtil.sendParticle(
          p, playerLocation.getX() - x, playerLocation.getY(), playerLocation.getZ() - z);
    }

    for (double angle = 0; angle < 2 * Math.PI; angle += angleIncrement) {
      final double y = crossDistance * Math.cos(angle);
      final double z = crossDistance * Math.sin(angle);
      MathUtil.sendParticle(
          p, playerLocation.getX(), playerLocation.getY() + y, playerLocation.getZ() + z);
      MathUtil.sendParticle(
          p, playerLocation.getX(), playerLocation.getY() - y, playerLocation.getZ() - z);
    }
  }

  public static void coloredSphereParticle(
      final Location location, final Color color, final double r) {
    for (final var player : Bukkit.getOnlinePlayers()) {
      coloredSphereParticle(player, location, color, r);
    }
  }

  public static void coloredSphereParticle(
      final Player player, final Location location, final Color color, final double r) {
    final double phiStep = Math.PI / 10;
    final double thetaStep = Math.PI / 40;

    final float locX = (float) location.getX();
    final float locY = (float) location.getY();
    final float locZ = (float) location.getZ();

    for (double phi = 0; phi <= Math.PI; phi += phiStep) {
      final double sinPhi = Math.sin(phi);
      final double cosPhi = Math.cos(phi);

      for (double theta = 0; theta <= 2 * Math.PI; theta += thetaStep) {
        final double cosTheta = Math.cos(theta);
        final double sinTheta = Math.sin(theta);

        final float x = (float) (r * cosTheta * sinPhi);
        final float y = (float) (r * cosPhi);
        final float z = (float) (r * sinTheta * sinPhi);

        final float particleX = locX + x;
        final float particleY = locY + y;
        final float particleZ = locZ + z;
        final Location particleLocation = location.clone();

        particleLocation.setY(particleY);
        particleLocation.setX(particleX);
        particleLocation.setZ(particleZ);

        spawnParticle(particleLocation, color.getRed(), color.getGreen(), color.getBlue(), player);
      }
    }
  }

  public static void coloredSphereParticleRadius(
      final Location location, final Color color, final double r) {
    final double phiStep = Math.PI / 10;
    final double thetaStep = Math.PI / 40;

    final float locX = (float) location.getX();
    final float locY = (float) location.getY();
    final float locZ = (float) location.getZ();

    for (double phi = 0; phi <= Math.PI; phi += phiStep) {
      final double sinPhi = Math.sin(phi);
      final double cosPhi = Math.cos(phi);

      for (double theta = 0; theta <= 2 * Math.PI; theta += thetaStep) {
        final double cosTheta = Math.cos(theta);
        final double sinTheta = Math.sin(theta);

        final float x = (float) (r * cosTheta * sinPhi);
        final float y = (float) (r * cosPhi);
        final float z = (float) (r * sinTheta * sinPhi);

        final float particleX = locX + x;
        final float particleY = locY + y;
        final float particleZ = locZ + z;
        final Location particleLocation = location.clone();
        particleLocation.setY(particleY);
        particleLocation.setX(particleX);
        particleLocation.setZ(particleZ);
      }
    }
  }

  public static void coloredPerfectSphereParticleRadius(
      final Location location, final int red, final int g, final int b, final double r) {
    //        final double phiStep = Math.PI / 10;
    final double phiStep = Math.PI / 20;
    final double thetaStep = Math.PI / 40;
    final double twoPi = 2 * Math.PI;

    final int phiSteps = (int) (Math.PI / phiStep) + 1;
    final int thetaSteps = (int) (twoPi / thetaStep) + 1;

    final double[] sinPhi = new double[phiSteps];
    final double[] cosPhi = new double[phiSteps];
    final double[] sinTheta = new double[thetaSteps];
    final double[] cosTheta = new double[thetaSteps];

    for (int i = 0; i < phiSteps; i++) {
      final double phi = i * phiStep;
      sinPhi[i] = Math.sin(phi);
      cosPhi[i] = Math.cos(phi);
    }

    for (int j = 0; j < thetaSteps; j++) {
      final double theta = j * thetaStep;
      sinTheta[j] = Math.sin(theta);
      cosTheta[j] = Math.cos(theta);
    }

    final double locX = location.getX();
    final double locY = location.getY();
    final double locZ = location.getZ();

    final Location particleLocation = location.clone();

    for (int i = 0; i < phiSteps; i++) {
      for (int j = 0; j < thetaSteps; j++) {
        final double x = r * cosTheta[j] * sinPhi[i];
        final double y = r * cosPhi[i];
        final double z = r * sinTheta[j] * sinPhi[i];

        particleLocation.setX(locX + x);
        particleLocation.setY(locY + y);
        particleLocation.setZ(locZ + z);

        spawnParticle(particleLocation, red, g, b);
      }
    }
  }

  public static void perfectSphereParticleRadius(
      final Location location, final EnumParticle enumParticle, final double r) {
    final double phiStep = Math.PI / 10;
    final double thetaStep = Math.PI / 40;

    final float locX = (float) location.getX();
    final float locY = (float) location.getY();
    final float locZ = (float) location.getZ();

    for (double phi = 0; phi <= Math.PI; phi += phiStep) {
      final double sinPhi = Math.sin(phi);
      final double cosPhi = Math.cos(phi);

      for (double theta = 0; theta <= 2 * Math.PI; theta += thetaStep) {
        final double cosTheta = Math.cos(theta);
        final double sinTheta = Math.sin(theta);

        final float x = (float) (r * cosTheta * sinPhi);
        final float y = (float) (r * cosPhi);
        final float z = (float) (r * sinTheta * sinPhi);

        final float particleX = locX + x;
        final float particleY = locY + y;
        final float particleZ = locZ + z;
        final Location particleLocation = location.clone();
        particleLocation.setY(particleY);
        particleLocation.setX(particleX);
        particleLocation.setZ(particleZ);
        MathUtil.sendParticle(enumParticle, particleLocation);
      }
    }
  }

  public static void vortexParticleEffect(
      final Location location,
      final int red,
      final int g,
      final int b,
      final double radius,
      final int height,
      final int revolutions) {
    final double phiStep = Math.PI / 8;
    final double thetaStep = Math.PI / 16;

    final float locX = (float) location.getX();
    final float locY = (float) location.getY();
    final float locZ = (float) location.getZ();

    final double heightStep = height / (phiStep * revolutions * 2 * Math.PI);

    for (double phi = 0; phi <= revolutions * 2 * Math.PI; phi += phiStep) {
      final double sinPhi = Math.sin(phi);
      final double cosPhi = Math.cos(phi);
      final double y = phi * heightStep;

      for (double theta = 0; theta <= 2 * Math.PI; theta += thetaStep) {
        final double cosTheta = Math.cos(theta);
        final double sinTheta = Math.sin(theta);

        final float x = (float) (radius * cosTheta * sinPhi);
        final float z = (float) (radius * sinTheta * sinPhi);

        final float particleX = locX + x;
        final float particleY = locY + (float) y;
        final float particleZ = locZ + z;

        final Location particleLocation =
            new Location(location.getWorld(), particleX, particleY, particleZ);
        spawnParticle(particleLocation, red, g, b);
      }
    }
  }

  public static void meteorParticleEffect(
      final Location location, final Color color, final double r) {
    final double phiStep = Math.PI / 10;
    final double thetaStep = Math.PI / 40;

    final double offsetY = 0;

    final float locX = (float) location.getX();
    final float locY = (float) location.getY();
    final float locZ = (float) location.getZ();

    for (double phi = 0; phi <= Math.PI; phi += phiStep) {
      final double sinPhi = Math.sin(phi);
      final double cosPhi = Math.cos(phi);

      for (double theta = 0; theta <= 2 * Math.PI; theta += thetaStep) {
        final double cosTheta = Math.cos(theta);
        final double sinTheta = Math.sin(theta);

        final float x = (float) (r * cosTheta * sinPhi);
        final float y = (float) (r * cosPhi);
        final float z = (float) (r * sinTheta * sinPhi);

        final float particleX = locX + x;
        final float particleY = locY + y;
        final float particleZ = locZ + z;
        final Location particleLocation = location.clone();
        particleLocation.setY(particleY);
        particleLocation.setX(particleX);
        particleLocation.setZ(particleZ);
      }
    }
  }

  public static void sendCircleParticle(
      final Color color,
      final Location center,
      final double radius,
      final int amount,
      final Player @NotNull ... player) {
    final double increment = 6.283185307179586 / amount;
    for (int i = 0; i < amount; ++i) {
      final double angle = i * increment;
      final double x = center.getX() + radius * Math.cos(angle);
      final double z = center.getZ() + radius * Math.sin(angle);
      final Location particleLocation = new Location(center.getWorld(), x, center.getY() + 0.5, z);
      spawnParticle(particleLocation, color.getRed(), color.getGreen(), color.getBlue(), player);
    }
  }

  public static void sendCircleParticle(
      final Color color, final Location center, final double radius, final int amount) {
    for (final var target : Bukkit.getOnlinePlayers()) {
      sendCircleParticle(color, center, radius, amount, target);
    }
  }

  public static void sendCircleParticle(
      final EnumParticle enumParticle,
      final Location center,
      final double radius,
      final int amount) {
    final double increment = 6.283185307179586 / amount;
    for (int i = 0; i < amount; ++i) {
      final double angle = i * increment;
      final double x = center.getX() + radius * Math.cos(angle);
      final double z = center.getZ() + radius * Math.sin(angle);
      final Location particleLocation = new Location(center.getWorld(), x, center.getY() + 0.5, z);
      MathUtil.sendParticle(enumParticle, particleLocation);
    }
  }

  public static void auraColor(final Location location, final int r, final int g, final int b) {
    for (final var target : Bukkit.getOnlinePlayers()) {
      auraColor(target, location, r, g, b);
    }
  }

  public static void auraColor(
      final Player player, final Location location, final int r, final int g, final int b) {

    for (int i = 0; i < 15; i++) {
      final double offsetX = Math.random() * 2 - 1;
      final double offsetY = Math.random() * 2;
      final double offsetZ = Math.random() * 2 - 1;

      spawnParticle(location.clone().add(offsetX, offsetY, offsetZ), r, g, b, player);
    }
  }

  public static void spawnParticle(
      final Location location, final float r, final float g, final float b) {
    final var red = Math.max(0.001F, r / 255);
    final var green = Math.max(0.001F, g / 255);
    final var blue = Math.max(0.001F, b / 255);

    final PacketPlayOutWorldParticles packet =
        new PacketPlayOutWorldParticles(
            EnumParticle.REDSTONE,
            true,
            (float) location.getX(),
            (float) location.getY(),
            (float) location.getZ(),
            red,
            green,
            blue,
            1.0f,
            0);

    CompletableFuture.runAsync(
        () -> {
          for (final Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
          }
        });
  }

  public static void spawnParticle(
      final Location location,
      final float r,
      final float g,
      final float b,
      final Player... players) {
    final PacketPlayOutWorldParticles packet =
        new PacketPlayOutWorldParticles(
            EnumParticle.REDSTONE,
            true,
            (float) location.getX(),
            (float) location.getY(),
            (float) location.getZ(),
            r / 255,
            g / 255,
            b / 255,
            1.0f,
            0);

    for (final var player : players) {
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
  }

  public static void spawnSpellParticle(
      final Location location, final float r, final float g, final float b) {
    final PacketPlayOutWorldParticles packet =
        new PacketPlayOutWorldParticles(
            EnumParticle.SPELL_MOB,
            true,
            (float) location.getX(),
            (float) location.getY(),
            (float) location.getZ(),
            r / 255.0f,
            g / 255.0f,
            b / 255.0f,
            1.0f,
            0);
    for (final Player player : Bukkit.getOnlinePlayers()) {
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
  }

  public static void spawnSpellParticle(
      final Location location,
      final float r,
      final float g,
      final float b,
      final Player... players) {
    final PacketPlayOutWorldParticles packet =
        new PacketPlayOutWorldParticles(
            EnumParticle.SPELL_MOB,
            true,
            (float) location.getX(),
            (float) location.getY(),
            (float) location.getZ(),
            r / 255.0f,
            g / 255.0f,
            b / 255.0f,
            1.0f,
            0);
    for (final Player player : players) {
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
  }

  public static void AuraPerfectColor(
      final Location location, final int r, final int g, final int b) {

    for (int i = 0; i < 30; i++) {
      final double offsetX = Math.random() * 2 - 1;
      final double offsetY = Math.random() * 2 - 1;
      final double offsetZ = Math.random() * 2 - 1;

      location.add(offsetX, offsetY, offsetZ);

      spawnParticle(location, r, g, b);
      location.subtract(offsetX, offsetY, offsetZ);
    }
  }

  public static void AuraSpellPerfectColor(
      final Location location, final int r, final int g, final int b) {
    for (int i = 0; i < 30; i++) {
      final double offsetX = Math.random() * 2 - 1;
      final double offsetY = Math.random() * 2 - 1;
      final double offsetZ = Math.random() * 2 - 1;

      location.add(offsetX, offsetY, offsetZ);

      spawnSpellParticle(location, r, g, b);
      location.subtract(offsetX, offsetY, offsetZ);
    }
  }

  public static void spawnGoodKamaEffect(
      final Player player, final float r, final float g, final float b) {
    final Location loc = player.getLocation().add(0, 1, 0);
    double angle = 0;
    for (int i = 0; i < 16; i++) {
      angle += Math.PI / 16;

      final double x = Math.cos(angle) * 0.5;
      final double z = Math.sin(angle) * 0.5;

      spawnParticle(loc.clone().add(x, 0, z), r, g, b);
      for (double j = 0; j <= Math.PI * 2; j += Math.PI / 8) {
        final double xOffset = Math.cos(j) * 0.5;
        final double zOffset = Math.sin(j) * 0.5;
        spawnParticle(loc.clone().add(xOffset, 0, zOffset), r, g, b);
      }
    }
  }

  public static void auraEffect(final Location location, final EnumParticle p) {
    for (int i = 0; i < 20; i++) {
      final double offsetX = Math.random() * 2 - 1;
      final double offsetY = Math.random() * 2 - 1;
      final double offsetZ = Math.random() * 2 - 1;

      MathUtil.sendParticle(p, location.clone().add(offsetX, offsetY, offsetZ));
    }
  }

  public static void playElectrocutionEffect(final Location location) {
    location.getWorld().playSound(location, Sound.AMBIENCE_THUNDER, 1, 1);
    for (int i = 0; i < 30; i++) {
      final double offsetX = Math.random() * 2 - 1;
      final double offsetY = Math.random() * 2 - 1;
      final double offsetZ = Math.random() * 2 - 1;

      location.add(offsetX, offsetY, offsetZ);

      location.subtract(offsetX, offsetY, offsetZ);
    }
  }

  public static void playBrumeEffect(final Location location) {
    for (int i = 0; i < 30; i++) {
      final double offsetX = Math.random() * 2 - 1;
      final double offsetY = Math.random() * 2 - 1;
      final double offsetZ = Math.random() * 2 - 1;
      location.add(offsetX, offsetY, offsetZ);

      location.subtract(offsetX, offsetY, offsetZ);
    }
  }

  public static void spawnVortexParticles(final Location location, final Vector direction) {
    final int particleCount = 100;
    final double radius = 1.5;
    final double rotationSpeed = 0.1;

    for (int i = 0; i < particleCount; i++) {
      final double angle = (2 * Math.PI * i) / particleCount;
      final double x = radius * Math.cos(angle);
      final double z = radius * Math.sin(angle);

      final double rotatedX = x * Math.cos(rotationSpeed) - z * Math.sin(rotationSpeed);
      final double rotatedZ = x * Math.sin(rotationSpeed) + z * Math.cos(rotationSpeed);

      final Vector rotatedVector = new Vector(rotatedX, 0, rotatedZ).add(direction);

      location.subtract(rotatedVector);
    }
  }

  private static Location interpolate(
      final Location start, final Location end, final float fraction) {
    final double x = start.getX() + (end.getX() - start.getX()) * fraction;
    final double y = start.getY() + (end.getY() - start.getY()) * fraction;
    final double z = start.getZ() + (end.getZ() - start.getZ()) * fraction;
    return new Location(start.getWorld(), x, y, z);
  }

  private static void spawnParticleLine(
      final Location start, final Location end, final Color color) {
    final double distance = start.distance(end);
    final Vector direction = end.toVector().subtract(start.toVector()).normalize();
    final Location current = start.clone();

    for (double i = 0; i < distance; i += 0.5) {

      current.add(direction);
    }
  }

  public static void spawnParticleLine(
      final Location from, final Location to, final EnumParticle p, final int count) {
    final double distance = from.distance(to);
    final Vector direction = to.toVector().subtract(from.toVector()).normalize();
    final double spacing = distance / count;
    for (int i = 0; i < count; i++) {
      final Location particleLocation = from.clone().add(direction.clone().multiply(spacing * i));
      MathUtil.sendParticle(p, particleLocation);
    }
  }

  public static void playDomeParticle(
      final @NotNull Player player,
      final @NotNull Location location,
      final @NotNull EnumParticle particle,
      final double radius,
      final double step) {
    for (double phi = 0; phi <= Math.PI / 2; phi += step) {
      for (double theta = 0; theta <= 2 * Math.PI; theta += step) {
        final double x = radius * Math.sin(phi) * Math.cos(theta);
        final double z = radius * Math.sin(phi) * Math.sin(theta);
        final double y = radius * Math.cos(phi);

        final Location particleLocation = location.clone().add(x, y, z);
        MathUtil.sendParticle(player, particle, particleLocation);
      }
    }
  }

  public static void spawnColoredLine(
      final @NotNull Location from,
      final @NotNull Location to,
      final int count,
      final float red,
      final float green,
      final float blue) {
    final double distance = from.distance(to);
    final Vector direction = to.toVector().subtract(from.toVector()).normalize();
    final double spacing = distance / count;

    for (int i = 0; i < count; i++) {
      final Location point = from.clone().add(direction.clone().multiply(spacing * i));
      spawnParticle(point, red, green, blue);
    }
  }
}
