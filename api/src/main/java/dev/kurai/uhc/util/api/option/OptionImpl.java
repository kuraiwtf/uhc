package dev.kurai.uhc.util.api.option;

import net.kyori.adventure.key.Key;

final class OptionImpl<T> implements Option<T> {

  private final Key key;
  private T value;

  public OptionImpl(final Key key, final T value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public Key key() {
    return this.key;
  }

  @Override
  public T getValue() {
    return this.value;
  }

  @Override
  public void setValue(final T value) {
    this.value = value;
  }
}
