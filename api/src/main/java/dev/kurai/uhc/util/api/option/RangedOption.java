package dev.kurai.uhc.util.api.option;

import net.kyori.adventure.key.Key;

final class RangedOption implements Option<Integer> {

  private final Key key;

  private final int min;
  private final int max;

  private int current;

  RangedOption(final Key key, final int min, final int max, final int defaultValue) {
    this.key = key;

    this.min = min;
    this.max = max;

    this.current = defaultValue;
  }

  @Override
  public Key key() {
    return this.key;
  }

  @Override
  public Integer getValue() {
    return this.current;
  }

  @Override
  public void setValue(final Integer value) {
    this.current = Math.clamp(value, this.min, this.max);
  }
}
