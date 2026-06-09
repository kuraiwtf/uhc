package dev.kurai.uhc.util.packet;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public sealed interface PacketWrapper permits MinecraftPacketWrapper, PacketEventsWrapper {

  static @NotNull PacketWrapper createPacketWrapper(
      final @NotNull com.github.retrooper.packetevents.wrapper.PacketWrapper<?> packet) {
    return new PacketEventsWrapper(packet);
  }

  static @NotNull PacketWrapper createPacketWrapper(final @NotNull Packet<?> packet) {
    return new MinecraftPacketWrapper(packet);
  }

  void send(final @NotNull Player player);

  default void send() {
    for (final var player : Bukkit.getOnlinePlayers()) {
      this.send(player);
    }
  }

  default void send(final @NotNull Iterable<? extends Player> players) {
    for (final var player : players) {
      this.send(player);
    }
  }
}
