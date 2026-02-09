package dev.kurai.uhc.game;

import static dev.kurai.uhc.util.api.option.Option.option;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.game.death.service.DeathService;
import dev.kurai.uhc.game.drop.service.DropRateService;
import dev.kurai.uhc.game.scatter.service.ScatterService;
import dev.kurai.uhc.game.scenario.service.ScenarioService;
import dev.kurai.uhc.game.start.service.StartService;
import dev.kurai.uhc.timer.service.TimerService;
import dev.kurai.uhc.util.api.option.Option;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GameService extends ForwardingAudience {

  Option<Integer> SLOTS_OPTION = option(key("slots"), 20);
  Option<Boolean> WHITELIST_OPTION = option(key("whitelist"), true);

  long getStartTime();

  void setStartTime(final @Range(from = 0L, to = Long.MAX_VALUE) long startTime);

  DeathService getDeathService();

  DropRateService getDropRateService();

  ScatterService getScatterService();

  ScenarioService getScenarioService();

  StartService getStartService();

  TimerService getTimerService();
}
