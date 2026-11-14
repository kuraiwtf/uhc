package dev.kurai.uhc.effect;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class EffectRefreshTask implements Runnable {

  public static final long INTERVAL_TICKS = 5 * 60 * 20L;

  private final EffectPacketSender sender;

  public EffectRefreshTask(final EffectPacketSender sender) {
    this.sender = sender;
  }

  @Override
  public void run() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      for (final EffectType effectType : EffectType.values()) {
        this.sender.send(player, effectType, false);
      }
    }
  }
}
