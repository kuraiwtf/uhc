package dev.kurai.uhc.scoreboard.sidebar.service;

import dev.kurai.uhc.scoreboard.sidebar.Sidebar;
import dev.kurai.uhc.scoreboard.sidebar.adapter.SidebarAdapter;
import dev.kurai.uhc.scoreboard.sidebar.adapter.SidebarTitleAdapter;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SidebarService {

  @Nullable
  SidebarTitleAdapter getTitleAdapter();

  void installTitleAdapter(final @Nullable SidebarTitleAdapter titleAdapter);

  @Nullable
  SidebarAdapter getAdapter();

  void installAdapter(final @Nullable SidebarAdapter adapter);

  default void install(
          final @NotNull SidebarTitleAdapter titleAdapter, final @NotNull SidebarAdapter adapter) {
    this.installTitleAdapter(titleAdapter);
    this.installAdapter(adapter);
  }

  void createScoreboard(final UUID uniqueId);

  void destroyScoreboard(final UUID uniqueId);

  boolean hasScoreboard(final UUID uniqueId);

  Sidebar getSidebar(final UUID uniqueId);
}
