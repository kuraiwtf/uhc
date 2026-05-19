package dev.kurai.uhc.scoreboard.sidebar;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SidebarAdapter {

  @NotNull
  List<@NotNull Component> provideLines(final @NotNull Player player);
}
