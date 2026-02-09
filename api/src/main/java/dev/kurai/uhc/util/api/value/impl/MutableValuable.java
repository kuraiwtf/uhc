package dev.kurai.uhc.util.api.value.impl;

import dev.kurai.uhc.util.api.value.Valuable;

public interface MutableValuable<T> extends Valuable<T> {

  void setValue(final T value);
}
