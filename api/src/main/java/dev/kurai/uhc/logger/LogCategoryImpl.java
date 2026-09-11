package dev.kurai.uhc.logger;

import dev.kurai.uhc.util.Color;

record LogCategoryImpl(String name, Color color) implements LogCategory {}
