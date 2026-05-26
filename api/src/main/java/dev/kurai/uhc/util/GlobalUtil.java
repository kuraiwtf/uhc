package dev.kurai.uhc.util;

import static org.bukkit.Material.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.player.PlayerExplosionEvent;
import dev.kurai.uhc.profile.Profile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityWeather;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class GlobalUtil {

  private GlobalUtil() {}

  public static void summonFakeLightning(final Location location) {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      summonFakeLightning(player, location);
    }
  }

  public static void summonFakeLightning(final Player viewer, final Location location) {
    summonFakeLightning(viewer, location, false);
  }

  public static void summonFakeLightning(
      final Player viewer, final Location location, final boolean effect) {
    final var handle = ((CraftPlayer) viewer).getHandle();
    final var lightning =
        new EntityLightning(
            handle.getWorld(), location.getX(), location.getY(), location.getZ(), effect, false);
    handle.playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(lightning));
    viewer.playSound(viewer.getLocation(), Sound.AMBIENCE_THUNDER, 1F, 1F);
  }

  public static List<@NotNull Location> spawnDome(
      final Location center, final double radius, final double density) {
    final List<Location> list = Lists.newArrayList();
    final World world = center.getWorld();
    if (world == null) {
      return list;
    }

    final double x0 = center.getX();
    final double y0 = center.getY();
    final double z0 = center.getZ();

    final double step = 1.0 / density;

    for (double x = -radius; x <= radius; x += step) {
      for (double y = 0; y <= radius; y += step) {
        for (double z = -radius; z <= radius; z += step) {
          final double distance = x * x + y * y + z * z;
          if (distance <= radius * radius && distance >= (radius - 0.5) * (radius - 0.5)) {
            final Location loc = new Location(world, x0 + x, y0 + y, z0 + z);
            list.add(loc);
          }
        }
      }
    }

    return list;
  }

  public static List<Location> createCircle(
      final Location origin, final int radius, final boolean borderOnly) {
    final List<Location> list = Lists.newArrayList();

    if (origin == null) {
      return list;
    }

    final int cX, cZ;
    cX = origin.getBlockX();
    cZ = origin.getBlockZ();

    for (int x = cX - radius; x <= cX + radius; x++) {
      for (int z = cZ - radius; z <= cZ + radius; z++) {
        final double distance = Math.pow(cX - x, 2) + Math.pow(cZ - z, 2);
        if (distance < Math.pow(radius, 2)) {
          final Location loc = new Location(origin.getWorld(), x, origin.getBlockY(), z);
          if (!borderOnly || distance >= Math.pow(radius - 1, 2)) {
            list.add(loc);
          }
        }
      }
    }

    return list;
  }

  public static List<Location> createSquare(final Location origin, final int radius) {
    final List<Location> list = Lists.newArrayList();

    if (origin == null) {
      return list;
    }

    final int cX, cY, cZ;
    cX = origin.getBlockX();
    cY = origin.getBlockY();
    cZ = origin.getBlockZ();

    for (int x = cX - radius; x <= cX + radius; x++) {
      for (int y = cY - radius; y <= cY + radius; y++) {
        for (int z = cZ - radius; z <= cZ + radius; z++) {
          final Location loc = new Location(origin.getWorld(), x, y, z);
          if ((x == cX - radius
              || x == cX + radius
              || y == cY - radius
              || y == cY + radius
              || z == cZ - radius
              || z == cZ + radius)) {
            list.add(loc);
          }
        }
      }
    }

    return list;
  }

  public static List<Location> createSphere(
      final Location origin, final int radius, final boolean borderOnly) {
    final List<Location> list = Lists.newArrayList();

    if (origin == null) {
      return list;
    }

    final int cX, cY, cZ;
    cX = origin.getBlockX();
    cY = origin.getBlockY();
    cZ = origin.getBlockZ();

    for (int x = cX - radius; x <= cX + radius; x++) {
      for (int y = cY - radius; y <= cY + radius; y++) {
        for (int z = cZ - radius; z <= cZ + radius; z++) {
          final double distance = Math.pow(cX - x, 2) + Math.pow(cY - y, 2) + Math.pow(cZ - z, 2);
          if (distance < Math.pow(radius, 2)) {
            final Location loc = new Location(origin.getWorld(), x, y, z);
            if (!borderOnly || distance >= Math.pow(radius - 1, 2)) {
              list.add(loc);
            }
          }
        }
      }
    }

    return list;
  }

  public static List<Location> createSphere(final Location origin, final int radius) {
    final List<Location> list = Lists.newArrayList();

    if (origin == null) {
      return list;
    }

    final int cX, cY, cZ;
    cX = origin.getBlockX();
    cY = origin.getBlockY();
    cZ = origin.getBlockZ();

    for (int x = cX - radius; x <= cX + radius; x++) {
      for (int y = cY - radius; y <= cY + radius; y++) {
        for (int z = cZ - radius; z <= cZ + radius; z++) {
          final double distance = Math.pow(cX - x, 2) + Math.pow(cY - y, 2) + Math.pow(cZ - z, 2);
          if (distance < Math.pow(radius, 2)) {
            final Location loc = new Location(origin.getWorld(), x, y, z);
            list.add(loc);
          }
        }
      }
    }

    return list;
  }

  public static void createBeautyExplosion(
      final Profile source, final Location loc, final int power) {
    createBeautyExplosion(source, loc, power, false);
  }

  public static void createBeautyExplosion(
      final Profile source, final Location loc, final int power, final boolean fire) {
    final List<Location> blocks = generateSphere(loc, power, false);
    final var explosionBlocks = Lists.<PlayerExplosionEvent.BlockData>newArrayList();
    for (final var location : blocks) {
      final var block = location.getBlock();
      explosionBlocks.add(
          new PlayerExplosionEvent.BlockData(
              block.getLocation(), block.getType(), block.getData()));
    }

    final var event =
        UltraHardcoreAPI.getInstance()
            .eventService()
            .dispatchEvent(
                new PlayerExplosionEvent(
                    source, new PlayerExplosionEvent.Explosion(explosionBlocks), loc, power));

    if (event.isCancelled()) {
      return;
    }

    for (final Location blockLoc : blocks) {
      final var block = blockLoc.getBlock();
      if (block.getType() != AIR && block.getType() != BEDROCK) {
        if (Math.random() < 0.05) {
          final var falling =
              loc.getWorld()
                  .spawnFallingBlock(blockLoc.add(0.5, 0, 0.5), block.getType(), block.getData());

          falling.setDropItem(false);
          falling.setHurtEntities(false);
          falling.setVelocity(
              blockLoc.toVector().subtract(loc.toVector()).normalize().multiply(0.6));
        }

        blockLoc.getBlock().setType(AIR);

        if (fire && Math.random() < 0.30) {
          final var above = block.getRelative(BlockFace.UP);

          if (above.getType() == AIR && block.getType().isSolid()) {
            above.setType(FIRE);
          }
        }
      }
    }
  }

  public static List<Location> generateSphere(
      final Location centerBlock, final int radius, final boolean hollow) {
    if (centerBlock == null) {
      return new ArrayList<>();
    }
    final List<Location> circleBlocks = new ArrayList<>();
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

  public static String getArrow(final Location from, final Location to) {
    if (from == null || to == null) {
      return "?";
    }
    if (!from.getWorld().getName().equals(to.getWorld().getName())) {
      return "?";
    }

    final String[] arrows = {"⬆", "⬈", "➡", "⬊", "⬇", "⬋", "⬅", "⬉", "⬆"};

    final var d = from.getDirection();
    final var v = to.subtract(from).toVector().normalize();
    double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
    a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));
    a = ((int) (a + 22.5D) % 360);
    if (a < 0.0D) {
      a += 360.0D;
    }
    return arrows[(int) a / 45];
  }

  public static @NotNull Collection<@NotNull Player> getPlayersAround(
      final @NotNull Player player, final double radius) {
    Preconditions.checkNotNull(player, "Player cannot be null.");
    Preconditions.checkArgument(radius > 0, "Radius must be greater than 0.");

    final var players = Lists.newArrayList(getPlayersAround(player.getLocation(), radius));
    players.remove(player);
    return players;
  }

  public static @NotNull @Unmodifiable Collection<@NotNull Player> getPlayersAround(
      final @NotNull Location location, final double radius) {
    Preconditions.checkNotNull(location, "Location cannot be null.");

    return location.getWorld().getNearbyEntities(location, radius, radius, radius).stream()
        .filter(Player.class::isInstance)
        .map(Player.class::cast)
        .toList();
  }

  public static Location getTargetLocation(final Player player, final int distance) {
    Location loc = player.getEyeLocation();

    for (int i = 0; i < distance; i++) {
      loc = loc.add(loc.getDirection());
      if (!loc.getBlock().getType().equals(AIR)) {
        break;
      }
    }

    return loc;
  }

  public static @Nullable Block getTargetBlock(final @NotNull Player player, final int distance) {
    final var iterator = new BlockIterator(player, distance);
    while (iterator.hasNext()) {
      final var block = iterator.next();
      if (!block.getType().equals(AIR)) {
        return block;
      }
    }

    return null;
  }

  public static Player getTargetPlayer(final Player player) {
    return getTarget(player, player.getWorld().getPlayers());
  }

  private static <T extends Entity> T getTarget(final Entity entity, final Iterable<T> entities) {
    if (entity == null) {
      return null;
    }

    T target = null;
    final double threshold = 2;

    for (final T other : entities) {
      final Vector n = other.getLocation().toVector().subtract(entity.getLocation().toVector());
      if (entity.getLocation().getDirection().normalize().crossProduct(n).lengthSquared()
              < threshold
          && n.normalize().dot(entity.getLocation().getDirection().normalize()) >= 0) {
        if (target == null
            || target.getLocation().distanceSquared(entity.getLocation())
                > other.getLocation().distanceSquared(entity.getLocation())) {
          target = other;
        }
      }
    }
    return target;
  }

  public static @NotNull List<Location> getArcBetween(
      final @NotNull Location origin,
      final @NotNull Location target,
      final double pointsPerBlock,
      final double radiusMultiplier,
      final double arcAngle,
      final int minimumPoints) {
    final var arcPoints = Lists.<Location>newArrayList();

    final var distance = origin.distance(target);
    final var radius = distance * radiusMultiplier;

    var points = (int) Math.ceil(radius * pointsPerBlock);
    if (points < minimumPoints) {
      points = minimumPoints;
    }

    final var direction = target.clone().subtract(origin).toVector();
    direction.setY(0);
    direction.normalize();

    final var startAngle = -Math.toRadians(arcAngle) / 2;

    for (int i = 0; i < points; i++) {
      final var angle = startAngle + (Math.toRadians(arcAngle) * i / (points - 1));
      final var rotated = rotateVector(direction.clone(), angle).multiply(radius);
      final var point = origin.clone().add(rotated);

      arcPoints.add(point);
    }

    return arcPoints;
  }

  private static Vector rotateVector(final Vector vec, final double angleRad) {
    final double x = vec.getX();
    final double z = vec.getZ();
    final double cos = Math.cos(angleRad);
    final double sin = Math.sin(angleRad);
    return new Vector(x * cos - z * sin, 0, x * sin + z * cos);
  }
}
