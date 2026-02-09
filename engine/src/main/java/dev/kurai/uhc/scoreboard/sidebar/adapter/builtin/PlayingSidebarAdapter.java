package dev.kurai.uhc.scoreboard.sidebar.adapter.builtin;

import static dev.kurai.uhc.util.GlobalUtil.getArrow;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.scoreboard.sidebar.adapter.SidebarAdapter;
import dev.kurai.uhc.util.TimeUtil;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayingSidebarAdapter implements SidebarAdapter {

  private final UltraHardcoreAPI ultraHardcore;

  public PlayingSidebarAdapter(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public @NotNull List<@NotNull Component> provideLines(final @NotNull Player player) {
    final var lines = Lists.<Component>newArrayList();
    final var tps = MinecraftServer.getServer().recentTps[0];
    lines.add(empty());
    lines.add(
        text()
            .append(text("Joueurs: "))
            .append(text(Bukkit.getOnlinePlayers().size(), GOLD, BOLD))
            .build());
    lines.add(
        text()
            .append(text("Temps: "))
            .append(
                text(
                    TimeUtil.formatDuration(
                        (System.currentTimeMillis()
                            - this.ultraHardcore.getGameService().getStartTime())),
                    GOLD,
                    BOLD))
            .build());
    lines.add(empty());

    final var module = this.ultraHardcore.getModuleService().getCurrentModule();
    if (module instanceof final SidebarAdapter moduleSidebar) {
      lines.addAll(moduleSidebar.provideLines(player));
      lines.add(empty());
    }

    lines.add(
        text()
            .append(text("Bordure: "))
            .append(text('±', GOLD, BOLD))
            .append(
                text(
                    "%.1f".formatted(player.getWorld().getWorldBorder().getSize() / 2), GOLD, BOLD))
            .build());

    final var centerLocation =
        new Location(player.getWorld(), 0.5, player.getLocation().getY(), 0.5);

    lines.add(
        text()
            .append(text("Centre: "))
            .append(text(getArrow(player.getLocation().clone(), centerLocation.clone()), GOLD))
            .appendSpace()
            .append(
                text("%.1f".formatted(centerLocation.distance(player.getLocation())), GOLD, BOLD))
            .append(text('m', GOLD))
            .build());
    lines.add(empty());
    lines.add(text().append(text('@', DARK_AQUA)).append(text("kuraiwtf", AQUA)).build());
    return lines;
  }
}
