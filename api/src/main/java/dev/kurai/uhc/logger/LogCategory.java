package dev.kurai.uhc.logger;

import dev.kurai.uhc.util.Color;

public interface LogCategory {

  static LogCategory logCategory(final String name, final Color color) {
    return new LogCategoryImpl(name, color);
  }

  String name();

  Color color();
}
