package dev.kurai.uhc.command;

import org.bukkit.command.CommandSender;

public interface PermissionProvider {

  boolean hasPermission(final CommandSender sender, final String permission);
}
