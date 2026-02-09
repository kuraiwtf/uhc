package dev.kurai.uhc.game.start.service;

import dev.kurai.uhc.game.start.phase.StartPhase;
import dev.kurai.uhc.game.start.phase.holder.StartPhaseHolder;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface StartService extends StartPhaseHolder {

  @Override
  Collection<@NotNull StartPhase> getPhases();

  @Override
  void clearPhases();

  @Override
  void registerPhase(final @NotNull StartPhase phase);

  @Override
  void unregisterPhase(final @NotNull String id);

  @Override
  boolean hasPhase(final @NotNull String id);

  boolean isStarting();

  void cancelStart();

  void handleStart();

  void handleFinalStart();
}
