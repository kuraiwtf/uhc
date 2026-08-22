package dev.kurai.uhc.util.api.state;

public interface Stateful<S extends State> {

  S getState();

  void setState(final S state);
}
