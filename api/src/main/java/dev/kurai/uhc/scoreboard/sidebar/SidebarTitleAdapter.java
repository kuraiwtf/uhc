package dev.kurai.uhc.scoreboard.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface SidebarTitleAdapter {

  Component provideTitle(final Player player);
}
