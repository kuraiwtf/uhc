package dev.kurai.uhc.win;

import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.team.TeamService;

public final class WinServiceImpl implements WinService {

  private WinCelebration celebration;
  private WinCondition condition;

  public WinServiceImpl(final ProfileService profileService, final TeamService teamService) {
    this.celebration = new BuiltinWinCelebration();
    this.condition = new BuiltinWinCondition(profileService, teamService);
  }

  @Override
  public WinCelebration winCelebration() {
    return this.celebration;
  }

  @Override
  public void installWinCelebration(final WinCelebration winCelebration) {
    this.celebration = winCelebration;
  }

  @Override
  public WinCondition winCondition() {
    return this.condition;
  }

  @Override
  public void installWinCondition(final WinCondition winCondition) {
    this.condition = winCondition;
  }
}
