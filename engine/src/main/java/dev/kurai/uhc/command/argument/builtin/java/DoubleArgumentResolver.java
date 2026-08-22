package dev.kurai.uhc.command.argument.builtin.java;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import dev.kurai.uhc.util.CC;
import org.bukkit.command.CommandSender;

public final class DoubleArgumentResolver implements ArgumentResolver<Double> {

  @Override
  public Double resolve(final CommandSender sender, final String argument) {
    try {
      return Double.parseDouble(argument);
    } catch (final NumberFormatException e) {
      sender.sendMessage(CC.prefix("&6%s&r n'est pas un double valide.".formatted(argument)));
      throw new RuntimeException(e);
    }
  }
}
