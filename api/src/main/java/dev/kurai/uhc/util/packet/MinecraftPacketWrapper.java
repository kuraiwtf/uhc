package dev.kurai.uhc.util.packet;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class MinecraftPacketWrapper implements PacketWrapper {

  private final Packet<?> packet;

  MinecraftPacketWrapper(final @NotNull Packet<?> packet) {
    this.packet = packet;
  }

  @Override
  public void send(final @NotNull Player player) {
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(this.packet);
  }
}
