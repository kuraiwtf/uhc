package dev.kurai.uhc.util.api.name.impl;

import dev.kurai.uhc.util.api.name.Nameable;

public interface MutableNameable<T> extends Nameable<T> {

  @Override
  T getName();

  void setName(final T name);
}
