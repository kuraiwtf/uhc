package dev.kurai.uhc.util.api.option.holder;

import dev.kurai.uhc.util.api.option.Option;
import java.util.Collection;

public interface OptionHolder {

  Collection<Option<?>> getOptions();

  void addOption(final Option<?> option);

  default void addOptions(final Option<?>... options) {
    for (final var option : options) {
      this.addOption(option);
    }
  }

  void removeOption(final Option<?> option);

  default void removeOptions(final Option<?>... options) {
    for (final var option : options) {
      this.removeOption(option);
    }
  }

  <T> Option<T> getOption(final String id);

  boolean hasOption(final String id);
}
