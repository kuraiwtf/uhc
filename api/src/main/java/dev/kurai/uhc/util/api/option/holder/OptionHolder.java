package dev.kurai.uhc.util.api.option.holder;

import dev.kurai.uhc.util.api.option.Option;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface OptionHolder {

  Collection<@NotNull Option<?>> getOptions();

  void addOption(final @NotNull Option<?> option);

  default void addOptions(final @NotNull Option<?> @NotNull ... options) {
    for (final var option : options) {
      this.addOption(option);
    }
  }

  void removeOption(final @NotNull Option<?> option);

  default void removeOptions(final @NotNull Option<?> @NotNull ... options) {
    for (final var option : options) {
      this.removeOption(option);
    }
  }

  <T> Option<T> getOption(final @NotNull String id);

  boolean hasOption(final @NotNull String id);
}
