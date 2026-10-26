package dev.kurai.uhc.game.disconnect;

public interface DisconnectService {

  int disconnectTime();

  void disconnectTime(final int disconnectTime);

  Strategy strategy();

  void strategy(final Strategy strategy);

  void start();

  enum Strategy {
    DISABLED,
    ONCE,
    CUMULATIVE
  }
}
