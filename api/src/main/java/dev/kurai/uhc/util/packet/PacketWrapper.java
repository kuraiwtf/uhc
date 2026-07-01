package dev.kurai.uhc.util.packet;

import java.util.function.Predicate;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public sealed interface PacketWrapper permits MinecraftPacketWrapper, PacketEventsWrapper {

  static PacketWrapper createPacketWrapper(
      final com.github.retrooper.packetevents.wrapper.PacketWrapper<?> packet) {
    return new PacketEventsWrapper(packet);
  }

  static PacketWrapper createPacketWrapper(final Packet<?> packet) {
    return new MinecraftPacketWrapper(packet);
  }

  void send(final Player player);

  default void send() {
    for (final var player : Bukkit.getOnlinePlayers()) {
      this.send(player);
    }
  }

  default void send(final Predicate<Player> filter) {
    this.send(Bukkit.getOnlinePlayers().stream().filter(filter).toList());
  }

  default void send(final Iterable<? extends Player> players) {
    for (final var player : players) {
      this.send(player);
    }
  }
}
