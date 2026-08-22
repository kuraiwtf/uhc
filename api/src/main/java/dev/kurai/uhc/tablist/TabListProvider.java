package dev.kurai.uhc.tablist;

import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface TabListProvider {

  ComponentLike provideComponent(final Player player);
}
