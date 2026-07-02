package dev.kurai.uhc.command.argument.builtin.java;

import dev.kurai.uhc.command.argument.ArgumentResolver;
import org.bukkit.command.CommandSender;

public final class StringArgumentResolver implements ArgumentResolver<String> {

  @Override
  public String resolve(final CommandSender sender, final String argument) {
    return argument;
  }
}
