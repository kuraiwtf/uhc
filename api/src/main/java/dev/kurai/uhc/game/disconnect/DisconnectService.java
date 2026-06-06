package dev.kurai.uhc.game.disconnect;

import org.jetbrains.annotations.Range;

public interface DisconnectService {

  long disconnectTime();

  void disconnectTime(
      final @Range(from = 5 * 60 * 1_000L, to = 20 * 60 * 1_000L) long disconnectTime);

  Strategy strategy();

  void strategy(final Strategy strategy);

  void start();

  enum Strategy {
    DISABLED,
    ONCE,
    CUMULATIVE
  }
}
