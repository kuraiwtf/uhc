package dev.kurai.uhc.tablist;

import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TabListProvider {

  @NotNull
  ComponentLike provideComponent(final @NotNull Player player);
}
