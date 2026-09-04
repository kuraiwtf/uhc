package dev.kurai.uhc.win;

import dev.kurai.uhc.profile.ProfileService;

public final class WinServiceImpl implements WinService {

  private WinCelebration celebration;
  private WinCondition condition;

  public WinServiceImpl(final ProfileService profileService) {
    this.celebration = new BuiltinWinCelebration();
    this.condition = new BuiltinWinCondition(profileService);
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
