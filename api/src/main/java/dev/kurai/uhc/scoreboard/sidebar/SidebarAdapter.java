package dev.kurai.uhc.scoreboard.sidebar;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface SidebarAdapter {

  List<Component> provideLines(final Player player);
}
