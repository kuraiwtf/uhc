package dev.kurai.uhc.util.api.option;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Contract;

public sealed interface Option<T> extends Keyed permits OptionImpl, RangedOption {

  @Contract(value = "_, _ -> new", pure = true)
  static <T> Option<T> option(final Key key, final T value) {
    return new OptionImpl<>(key, value);
  }

  @Contract(value = "_, _ -> new", pure = true)
  static Option<Integer> positiveNumberOption(final Key key, final int defaultValue) {
    return rangedOption(key, 0, Integer.MAX_VALUE, defaultValue);
  }

  @Contract(value = "_, _, _, _ -> new", pure = true)
  static Option<Integer> rangedOption(
      final Key key, final int min, final int max, final int defaultValue) {
    return new RangedOption(key, min, max, defaultValue);
  }

  @Override
  Key key();

  T getValue();

  void setValue(T value);
}
