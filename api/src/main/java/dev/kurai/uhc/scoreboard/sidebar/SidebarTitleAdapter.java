package dev.kurai.uhc.scoreboard.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface SidebarTitleAdapter {

  @NotNull
  Component provideTitle(final @NotNull Player player);
}
