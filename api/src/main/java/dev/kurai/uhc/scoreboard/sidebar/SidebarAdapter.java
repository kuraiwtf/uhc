package dev.kurai.uhc.scoreboard.sidebar;

import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

public interface SidebarAdapter {

    Component SEPARATOR = text().append(text(CC.BAR, DARK_GRAY)).appendSpace().build();

    List<Component> provideLines(final Player player);
}
