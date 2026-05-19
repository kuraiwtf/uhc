package dev.kurai.uhc.game;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.death.service.DeathService;
import dev.kurai.uhc.game.death.service.DeathServiceImpl;
import dev.kurai.uhc.game.drop.defaults.AppleDropRateModifier;
import dev.kurai.uhc.game.drop.defaults.FlintDropRateModifier;
import dev.kurai.uhc.game.drop.service.DropRateService;
import dev.kurai.uhc.game.drop.service.DropRateServiceImpl;
import dev.kurai.uhc.game.episode.EpisodeService;
import dev.kurai.uhc.game.episode.EpisodeServiceImpl;
import dev.kurai.uhc.game.scatter.service.ScatterService;
import dev.kurai.uhc.game.scatter.service.ScatterServiceImpl;
import dev.kurai.uhc.game.scenario.service.ScenarioService;
import dev.kurai.uhc.game.scenario.service.ScenarioServiceImpl;
import dev.kurai.uhc.game.start.service.StartService;
import dev.kurai.uhc.game.start.service.StartServiceImpl;
import dev.kurai.uhc.timer.builtin.BorderTimer;
import dev.kurai.uhc.timer.builtin.InvincibilityTimer;
import dev.kurai.uhc.timer.builtin.PvPTimer;
import dev.kurai.uhc.timer.service.TimerService;
import dev.kurai.uhc.timer.service.TimerServiceImpl;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GameServiceImpl implements GameService {

  private final UltraHardcoreAPI ultraHardcore;

  private final DeathService deathService;
  private final DropRateService dropRateService;
  private final EpisodeService episodeService;
  private final ScatterService scatterService;
  private final ScenarioService scenarioService;
  private final StartService startService;
  private final TimerService timerService;

  private long startTime;

  public GameServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;

    this.deathService = new DeathServiceImpl(ultraHardcore);
    (this.dropRateService = new DropRateServiceImpl(ultraHardcore.getEventService()))
        .registerModifiers(new AppleDropRateModifier(), new FlintDropRateModifier());
    this.episodeService = new EpisodeServiceImpl(ultraHardcore);
    this.scatterService = new ScatterServiceImpl(ultraHardcore, this);
    this.scenarioService = new ScenarioServiceImpl(ultraHardcore);

    final var bukkitAudiences = ultraHardcore.getBukkitAudiences();

    this.startService = new StartServiceImpl(ultraHardcore);

    (this.timerService = new TimerServiceImpl(ultraHardcore.getPlugin()))
        .registerTimers(
            new InvincibilityTimer(bukkitAudiences, ultraHardcore),
            new PvPTimer(bukkitAudiences),
            new BorderTimer(bukkitAudiences));
  }

  @Override
  public long getStartTime() {
    return this.startTime;
  }

  @Override
  public void setStartTime(@Range(from = 0L, to = Long.MAX_VALUE) final long startTime) {
    this.startTime = startTime;
  }

  @Override
  public DeathService getDeathService() {
    return this.deathService;
  }

  @Override
  public DropRateService getDropRateService() {
    return this.dropRateService;
  }

  @Override
  public EpisodeService getEpisodeService() {
    return this.episodeService;
  }

  @Override
  public ScatterService getScatterService() {
    return this.scatterService;
  }

  @Override
  public ScenarioService getScenarioService() {
    return this.scenarioService;
  }

  @Override
  public StartService getStartService() {
    return this.startService;
  }

  @Override
  public TimerService getTimerService() {
    return this.timerService;
  }

  @Override
  public Iterable<? extends Audience> audiences() {
    return this.ultraHardcore
        .getProfileService()
        .getProfiles(profile -> profile.findPlayer().isPresent());
  }
}
