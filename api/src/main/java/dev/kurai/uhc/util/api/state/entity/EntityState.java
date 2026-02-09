package dev.kurai.uhc.util.api.state.entity;

import dev.kurai.uhc.util.api.state.State;
import org.jetbrains.annotations.NotNull;

public interface EntityState<E> extends State {

  @Override
  @NotNull
  String getId();

  void onEntry(final E entity);

  void onExit(final E entity);
}
