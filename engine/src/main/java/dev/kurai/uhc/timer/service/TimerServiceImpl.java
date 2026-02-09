package dev.kurai.uhc.timer.service;

import dev.kurai.uhc.timer.AbstractTimer;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class TimerServiceImpl implements TimerService {

  private final Map<String, AbstractTimer> timers;
  private final Plugin plugin;

  public TimerServiceImpl(final @NotNull Plugin plugin) {
    this.timers = new ConcurrentHashMap<>();
    this.plugin = plugin;
  }

  @Override
  public void registerTimer(final @NotNull AbstractTimer timer) {
    this.timers.put(timer.getIdentifier(), timer);
  }

  @Override
  public void unregisterTimer(final @NotNull String identifier) {
    final var timer = this.timers.remove(identifier);
    if (timer != null && timer.isRunning()) {
      timer.stop();
    }
  }

  @Override
  public @NotNull Optional<AbstractTimer> getTimer(final @NotNull String identifier) {
    return Optional.ofNullable(this.timers.get(identifier));
  }

  @Override
  public @NotNull Collection<AbstractTimer> getTimers() {
    return this.timers.values();
  }

  @Override
  public void startAllTimers() {
    this.timers.values().forEach(timer -> timer.start(this.plugin));
  }

  @Override
  public void stopAllTimers() {
    this.timers.values().forEach(AbstractTimer::stop);
  }
}
