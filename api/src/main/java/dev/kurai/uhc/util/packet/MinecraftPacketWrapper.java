package dev.kurai.uhc.util.packet;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

final class MinecraftPacketWrapper implements PacketWrapper {

  private final Packet<?> packet;

  MinecraftPacketWrapper(final Packet<?> packet) {
    this.packet = packet;
  }

  @Override
  public void send(final Player player) {
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(this.packet);
  }
}
