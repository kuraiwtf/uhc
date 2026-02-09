package dev.kurai.uhc.util.api.state;

import org.jetbrains.annotations.NotNull;

public interface Stateful<S extends State> {

  S getState();

  void setState(final @NotNull S state);
}
