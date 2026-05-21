package dev.kurai.uhc.scoreboard.sidebar.adapter.builtin;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.team.module.TeamModule;
import dev.kurai.uhc.scoreboard.sidebar.SidebarAdapter;
import dev.kurai.uhc.scoreboard.sidebar.SidebarTitleAdapter;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class WaitingSidebarAdapter implements SidebarAdapter, SidebarTitleAdapter {

  private final UltraHardcoreAPI ultraHardcore;

  public WaitingSidebarAdapter(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public @NotNull Component provideTitle(final @NotNull Player player) {
    return text()
        .append(text('-', DARK_GRAY, BOLD))
        .appendSpace()
        .append(text("UHC", GOLD, BOLD))
        .appendSpace()
        .append(text('-', DARK_GRAY, BOLD))
        .build();
  }

  @Override
  public @NotNull List<@NotNull Component> provideLines(final @NotNull Player player) {
    final var lines = Lists.<Component>newArrayList();
    final var gameService = this.ultraHardcore.gameService();
    final var hostId = gameService.hostService().host();
    lines.add(empty());
    lines.add(
        text()
            .append(text("Hôte: "))
            .append(
                text(
                    hostId == null
                        ? "Aucun"
                        : this.ultraHardcore.profileService().getOrCreateProfile(hostId).getName(),
                    hostId == null ? RED : GOLD))
            .build());
    final var module = this.ultraHardcore.moduleService().getCurrentModule();
    lines.add(text().append(text("Jeu: ")).append(text(module.getName(), GOLD, BOLD)).build());
    lines.add(empty());
    lines.add(
        text()
            .append(text("Joueurs: "))
            .append(text(Bukkit.getOnlinePlayers().size(), GOLD, BOLD))
            .append(text('/', DARK_GRAY))
            .append(text(gameService.slotService().slotProvider().slots(), GOLD))
            .build());
    if (module instanceof final TeamModule teamModule) {
      lines.add(
          text()
              .append(text("Équipes: "))
              .append(
                  text(
                      teamModule.teamSize() == 1
                          ? "FFA"
                          : "%dvs%d".formatted(teamModule.teamSize(), teamModule.teamSize()),
                      GOLD))
              .build());
    }
    lines.add(empty());
    lines.add(text().append(text('@', DARK_AQUA)).append(text("kuraiwtf", AQUA)).build());
    return lines;
  }

  private @NotNull TextColor provideTpsColor(final double tps) {
    return tps > 15 ? GREEN : tps > 10 ? YELLOW : tps > 5 ? GOLD : RED;
  }
}
