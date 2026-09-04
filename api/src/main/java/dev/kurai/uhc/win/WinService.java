package dev.kurai.uhc.win;

public interface WinService {

  WinCelebration winCelebration();

  void installWinCelebration(final WinCelebration winCelebration);

  WinCondition winCondition();

  void installWinCondition(final WinCondition winCondition);
}
