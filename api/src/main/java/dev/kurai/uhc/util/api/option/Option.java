package dev.kurai.uhc.util.api.option;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface Option<T> extends Keyed permits OptionImpl {

  @Contract(value = "_, _ -> new", pure = true)
  static <T> @NotNull Option<T> option(final @NotNull Key key, final T value) {
    return new OptionImpl<>(key, value);
  }

  @Override
  @NotNull
  Key key();

  T getValue();

  void setValue(T value);
}
