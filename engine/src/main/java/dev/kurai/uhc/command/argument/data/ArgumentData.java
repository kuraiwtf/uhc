package dev.kurai.uhc.command.argument.data;

import org.jetbrains.annotations.NotNull;

public record ArgumentData(String name, String defaultValue) {

  public ArgumentData(final  String name, final  String defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  @Override public  String name() {
    return name;
  }

  @Override public  String defaultValue() {
    return defaultValue;
  }
}
