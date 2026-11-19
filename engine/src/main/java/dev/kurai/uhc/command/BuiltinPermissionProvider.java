package dev.kurai.uhc.command;

import org.bukkit.command.CommandSender;

public final class BuiltinPermissionProvider implements PermissionProvider {

    @Override
    public boolean hasPermission(final CommandSender sender, final String permission) {
        return permission.isEmpty() || sender.hasPermission(permission);
    }
}
