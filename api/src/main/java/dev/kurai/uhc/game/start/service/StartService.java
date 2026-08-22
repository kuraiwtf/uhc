package dev.kurai.uhc.game.start.service;

import dev.kurai.uhc.game.start.phase.StartPhase;
import dev.kurai.uhc.game.start.phase.holder.StartPhaseHolder;
import java.util.Collection;

public interface StartService extends StartPhaseHolder {

  @Override
  Collection<StartPhase> getPhases();

  @Override
  void clearPhases();

  @Override
  void registerPhase(final StartPhase phase);

  @Override
  void unregisterPhase(final String id);

  @Override
  boolean hasPhase(final String id);

  boolean isStarting();

  void cancelStart();

  void handleStart();

  void handleFinalStart();
}
