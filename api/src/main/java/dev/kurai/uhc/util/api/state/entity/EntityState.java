package dev.kurai.uhc.util.api.state.entity;

import dev.kurai.uhc.util.api.state.State;

public interface EntityState<E> extends State {

  @Override
  String getId();

  void onEntry(final E entity);

  void onExit(final E entity);
}
