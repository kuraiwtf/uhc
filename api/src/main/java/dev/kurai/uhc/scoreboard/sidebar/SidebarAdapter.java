package dev.kurai.uhc.scoreboard.sidebar;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

import dev.kurai.uhc.util.CC;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface SidebarAdapter {

  Component SEPARATOR = text().append(text(CC.BAR, DARK_GRAY)).appendSpace().build();

  List<Component> provideLines(final Player player);
}
