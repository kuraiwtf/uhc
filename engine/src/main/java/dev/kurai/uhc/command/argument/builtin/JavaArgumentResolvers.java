package dev.kurai.uhc.command.argument.builtin;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import org.jetbrains.annotations.NotNull;

public final class JavaArgumentResolvers {

  public static final ArgumentResolver<@NotNull String> STRING_RESOLVER = (_, argument) -> argument;

  public static final ArgumentResolver<@NotNull Integer> INTEGER_RESOLVER =
      (sender, argument) -> {
        try {
          return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
          sender.sendMessage("§c%s is not a valid integer.".formatted(argument));
          throw new RuntimeException(e);
        }
      };

  public static final ArgumentResolver<@NotNull Boolean> BOOLEAN_RESOLVER =
      (sender, argument) -> {
        try {
          return Boolean.parseBoolean(argument);
        } catch (Exception e) {
          sender.sendMessage("§c%s is not a valid boolean.".formatted(argument));
          throw new RuntimeException(e);
        }
      };

  public static final ArgumentResolver<@NotNull Double> DOUBLE_RESOLVER =
      (sender, argument) -> {
        try {
          return Double.parseDouble(argument);
        } catch (NumberFormatException e) {
          sender.sendMessage("§c%s is not a valid double.".formatted(argument));
          throw new RuntimeException(e);
        }
      };

  public static final ArgumentResolver<@NotNull Long> LONG_RESOLVER =
      (sender, argument) -> {
        try {
          return Long.parseLong(argument);
        } catch (NumberFormatException e) {
          sender.sendMessage("§c%s is not a valid long.".formatted(argument));
          throw new RuntimeException(e);
        }
      };
}
