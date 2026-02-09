package dev.kurai.uhc.util.api.key.impl;

import dev.kurai.uhc.util.api.key.Keyed;

public interface MutableKeyed<T> extends Keyed<T> {

  @Override
  T getKey();

  void setKey(final T key);
}
