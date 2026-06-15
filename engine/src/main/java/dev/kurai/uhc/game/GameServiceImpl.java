package dev.kurai.uhc.game;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.cycle.CycleService;
import dev.kurai.uhc.game.cycle.CycleServiceImpl;
import dev.kurai.uhc.game.death.DeathService;
import dev.kurai.uhc.game.death.DeathServiceImpl;
import dev.kurai.uhc.game.disconnect.DisconnectService;
import dev.kurai.uhc.game.disconnect.DisconnectServiceImpl;
import dev.kurai.uhc.game.drop.DropRateService;
import dev.kurai.uhc.game.drop.DropRateServiceImpl;
import dev.kurai.uhc.game.drop.defaults.AppleDropRateModifier;
import dev.kurai.uhc.game.drop.defaults.FlintDropRateModifier;
import dev.kurai.uhc.game.episode.EpisodeService;
import dev.kurai.uhc.game.episode.EpisodeServiceImpl;
import dev.kurai.uhc.game.host.HostService;
import dev.kurai.uhc.game.host.HostServiceImpl;
import dev.kurai.uhc.game.scatter.ScatterService;
import dev.kurai.uhc.game.scatter.ScatterServiceImpl;
import dev.kurai.uhc.game.scenario.ScenarioService;
import dev.kurai.uhc.game.scenario.ScenarioServiceImpl;
import dev.kurai.uhc.game.slot.SlotService;
import dev.kurai.uhc.game.slot.SlotServiceImpl;
import dev.kurai.uhc.game.start.StartServiceImpl;
import dev.kurai.uhc.game.start.service.StartService;
import dev.kurai.uhc.timer.TimerService;
import dev.kurai.uhc.timer.TimerServiceImpl;
import dev.kurai.uhc.timer.builtin.BorderTimer;
import dev.kurai.uhc.timer.builtin.InvincibilityTimer;
import dev.kurai.uhc.timer.builtin.PvPTimer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Getter
@Setter
public final class GameServiceImpl implements GameService {

  private final UltraHardcoreAPI ultraHardcore;

  private final CycleService cycleService;
  private final DeathService deathService;
  private final DisconnectService disconnectService;
  private final DropRateService dropRateService;
  private final EpisodeService episodeService;
  private final HostService hostService;
  private final ScatterService scatterService;
  private final ScenarioService scenarioService;
  private final SlotService slotService;
  private final StartService startService;
  private final TimerService timerService;

  private long startTime;

  public GameServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;

    this.cycleService = new CycleServiceImpl(ultraHardcore);
    this.deathService = new DeathServiceImpl(ultraHardcore);
    this.disconnectService = new DisconnectServiceImpl(ultraHardcore);
    (this.dropRateService = new DropRateServiceImpl(ultraHardcore.eventService()))
        .registerModifiers(new AppleDropRateModifier(), new FlintDropRateModifier());
    this.episodeService = new EpisodeServiceImpl(ultraHardcore);
    this.hostService = new HostServiceImpl(ultraHardcore);
    this.scatterService = new ScatterServiceImpl(ultraHardcore);
    this.scenarioService = new ScenarioServiceImpl(ultraHardcore);
    this.slotService = new SlotServiceImpl();
    this.startService = new StartServiceImpl(ultraHardcore);

    final var bukkitAudiences = ultraHardcore.bukkitAudiences();
    (this.timerService = new TimerServiceImpl(ultraHardcore.plugin()))
        .registerTimers(
            new InvincibilityTimer(ultraHardcore),
            new PvPTimer(bukkitAudiences),
            new BorderTimer(bukkitAudiences));
  }

  @Override
  public Iterable<? extends Audience> audiences() {
    return this.ultraHardcore
        .profileService()
        .getProfiles(profile -> profile.findPlayer().isPresent());
  }
}
