package dev.kurai.uhc.game;

import static dev.kurai.uhc.util.api.option.Option.option;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.game.cycle.CycleService;
import dev.kurai.uhc.game.death.DeathService;
import dev.kurai.uhc.game.disconnect.DisconnectService;
import dev.kurai.uhc.game.drop.DropRateService;
import dev.kurai.uhc.game.episode.EpisodeService;
import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.game.scatter.ScatterService;
import dev.kurai.uhc.game.scenario.ScenarioService;
import dev.kurai.uhc.game.slot.SlotService;
import dev.kurai.uhc.game.start.service.StartService;
import dev.kurai.uhc.timer.TimerService;
import dev.kurai.uhc.util.api.option.Option;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GameService extends ForwardingAudience {

  Option<Boolean> WHITELIST_OPTION = option(key("whitelist"), true);

  long startTime();

  void startTime(final @Range(from = 0L, to = Long.MAX_VALUE) long startTime);

  CycleService cycleService();

  DeathService deathService();

  DisconnectService disconnectService();

  DropRateService dropRateService();

  EpisodeService episodeService();

  HostService hostService();

  ScatterService scatterService();

  ScenarioService scenarioService();

  SlotService slotService();

  StartService startService();

  TimerService timerService();
}
