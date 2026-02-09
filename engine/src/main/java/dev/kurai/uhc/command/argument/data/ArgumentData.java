package dev.kurai.uhc.command.argument.data;

import org.jetbrains.annotations.NotNull;

public record ArgumentData(String name, String defaultValue) {

  public ArgumentData(final @NotNull String name, final @NotNull String defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  @Override public @NotNull String name() {
    return name;
  }

  @Override public @NotNull String defaultValue() {
    return defaultValue;
  }
}
