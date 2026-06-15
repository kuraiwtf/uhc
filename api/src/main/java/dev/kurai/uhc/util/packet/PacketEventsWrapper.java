package dev.kurai.uhc.util.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class PacketEventsWrapper implements PacketWrapper {

  private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

  private final com.github.retrooper.packetevents.wrapper.PacketWrapper<?> packet;

  PacketEventsWrapper(final com.github.retrooper.packetevents.wrapper.PacketWrapper<?> packet) {
    this.packet = packet;
  }

  @Override
  public void send(final  Player player) {
    PLAYER_MANAGER.sendPacket(player, this.packet);
  }
}
