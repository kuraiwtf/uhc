package dev.kurai.uhc.command.argument.builtin.java;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import dev.kurai.uhc.util.CC;
import org.bukkit.command.CommandSender;

public final class BooleanArgumentResolver implements ArgumentResolver<Boolean> {

  @Override
  public Boolean resolve(final CommandSender sender, final String argument) {
    try {
      return Boolean.parseBoolean(argument);
    } catch (final Exception e) {
      sender.sendMessage(CC.prefix("&6%s&r n'est pas un booléen valide.".formatted(argument)));
      throw new RuntimeException(e);
    }
  }
}
