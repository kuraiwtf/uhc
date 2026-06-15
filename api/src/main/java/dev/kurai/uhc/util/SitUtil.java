package dev.kurai.uhc.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAttachEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.kurai.uhc.event.defaults.sit.PlayerSitEvent;
import dev.kurai.uhc.event.defaults.sit.PlayerUnsitEvent;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class SitUtil {

  private static final Map<UUID, Integer> SEATS = Maps.newHashMap();

  public static void sit(final Player player, final Plugin plugin) {
    sit(player, -1L, plugin);
  }

  public static void sit(final Player player, final long ticks, final Plugin plugin) {
    if (isSit(player)) {
      removeFromSit(player);
    }

    final var event = new PlayerSitEvent(player, ticks);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      return;
    }

    final var fakeEntity = SpigotReflectionUtil.generateEntityId();
    final var spawnPacket =
        new WrapperPlayServerSpawnEntity(
            fakeEntity,
            UUID.randomUUID(),
            EntityTypes.ARMOR_STAND,
            SpigotConversionUtil.fromBukkitLocation(
                player.getLocation().clone().add(0, -player.getEyeHeight(), 0)),
            0F,
            0,
            new Vector3d(0, 0, 0));

    final var attachPacket =
        new WrapperPlayServerAttachEntity(player.getEntityId(), fakeEntity, false);

    final var metadata = Lists.<EntityData<?>>newArrayList();
    metadata.add(new EntityData<>(0, EntityDataTypes.BYTE, (byte) 0x20));

    final var metadataPacket = new WrapperPlayServerEntityMetadata(fakeEntity, metadata);

    final var playerManager = PacketEvents.getAPI().getPlayerManager();
    final var user = playerManager.getUser(player);
    user.sendPacket(spawnPacket);
    user.sendPacket(attachPacket);
    user.sendPacket(metadataPacket);

    SEATS.put(player.getUniqueId(), fakeEntity);

    if (event.getDurationInTicks() == -1) {
      return;
    }

    Bukkit.getScheduler()
        .runTaskLater(plugin, () -> removeFromSit(player), event.getDurationInTicks());
  }

  public static void removeFromSit(final Player player) {
    if (!isSit(player)) {
      return;
    }

    final var event = new PlayerUnsitEvent(player);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      return;
    }

    final var playerManager = PacketEvents.getAPI().getPlayerManager();
    final var user = playerManager.getUser(player);

    final var detachPacket = new WrapperPlayServerAttachEntity(player.getEntityId(), -1, false);
    user.sendPacket(detachPacket);

    final var removePacket =
        new WrapperPlayServerDestroyEntities(SEATS.remove(player.getUniqueId()));
    user.sendPacket(removePacket);
  }

  public static boolean isSit(final Player player) {
    return isSit(player.getUniqueId());
  }

  public static boolean isSit(final UUID uniqueId) {
    return SEATS.containsKey(uniqueId);
  }
}
