package dev.kurai.uhc.command.argument.builtin.java;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import dev.kurai.uhc.util.CC;
import org.bukkit.command.CommandSender;

public final class LongArgumentResolver implements ArgumentResolver<Long> {

  @Override
  public Long resolve(final CommandSender sender, final String argument) {
    try {
      return Long.parseLong(argument);
    } catch (final NumberFormatException e) {
      sender.sendMessage(CC.prefix("&6%s&r n'est pas un long valide.".formatted(argument)));
      throw new RuntimeException(e);
    }
  }
}
