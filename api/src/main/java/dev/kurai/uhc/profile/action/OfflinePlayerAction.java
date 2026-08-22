package dev.kurai.uhc.profile.action;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface OfflinePlayerAction {

  void onJoin(final Player player);
}
