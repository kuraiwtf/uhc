package dev.kurai.uhc.scoreboard.sidebar.task.updater;

import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class SidebarUpdaterTask implements Runnable {

  private final SidebarService sidebarService;

  public SidebarUpdaterTask(final  SidebarService sidebarService) {
    this.sidebarService = sidebarService;
  }

  @Override
  public void run() {
    final var titleAdapter = this.sidebarService.getTitleAdapter();
    final var adapter = this.sidebarService.getAdapter();

    if (adapter == null || titleAdapter == null) {
      return;
    }

    for (final var player : Bukkit.getServer().getOnlinePlayers()) {
      final var sidebar = this.sidebarService.getSidebar(player.getUniqueId());
      if (sidebar == null) {
        continue;
      }

      sidebar.editTitle(titleAdapter.provideTitle(player));

      int score = 15;
      final var providedLines = adapter.provideLines(player);

      for (final var entry : providedLines) {
        sidebar.overrideLine(score--, entry);
      }

      sidebar.send();
    }
  }
}
