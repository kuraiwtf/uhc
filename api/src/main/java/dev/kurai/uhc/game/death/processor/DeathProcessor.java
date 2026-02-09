package dev.kurai.uhc.game.death.processor;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DeathProcessor {

  void processDeath(final @NotNull PlayerDeathEvent event);
}
