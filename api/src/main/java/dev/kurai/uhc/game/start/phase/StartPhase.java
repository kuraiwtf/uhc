package dev.kurai.uhc.game.start.phase;

import dev.kurai.uhc.util.api.Identifiable;
import org.jetbrains.annotations.NotNull;

public interface StartPhase extends Identifiable<@NotNull String> {

  @Override
  @NotNull
  String getId();

  void start();

  void cancel();
}
