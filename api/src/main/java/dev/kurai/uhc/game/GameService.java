package dev.kurai.uhc.game;

import static dev.kurai.uhc.util.api.option.Option.option;
import static dev.kurai.uhc.util.api.option.Option.positiveNumberOption;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.game.death.DeathService;
import dev.kurai.uhc.game.drop.DropRateService;
import dev.kurai.uhc.game.episode.EpisodeService;
import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.game.scatter.ScatterService;
import dev.kurai.uhc.game.scenario.ScenarioService;
import dev.kurai.uhc.game.start.service.StartService;
import dev.kurai.uhc.timer.TimerService;
import dev.kurai.uhc.util.api.option.Option;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GameService extends ForwardingAudience {

  Option<Integer> SLOTS_OPTION = positiveNumberOption(key("slots"), 20);
  Option<Boolean> WHITELIST_OPTION = option(key("whitelist"), true);

  long startTime();

  void startTime(final @Range(from = 0L, to = Long.MAX_VALUE) long startTime);

  DeathService deathService();

  DropRateService dropRateService();

  EpisodeService episodeService();

  HostService hostService();

  ScatterService scatterService();

  ScenarioService scenarioService();

  StartService startService();

  TimerService timerService();
}
