package dev.kurai.uhc.game.start.phase;

import dev.kurai.uhc.util.api.Identifiable;

public interface StartPhase extends Identifiable<String> {

  @Override
  String getId();

  void start();

  void cancel();
}
