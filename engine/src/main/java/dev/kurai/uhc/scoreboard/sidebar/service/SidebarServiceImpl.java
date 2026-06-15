package dev.kurai.uhc.scoreboard.sidebar.service;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.scoreboard.sidebar.Sidebar;
import dev.kurai.uhc.scoreboard.sidebar.SidebarAdapter;
import dev.kurai.uhc.scoreboard.sidebar.SidebarImpl;
import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import dev.kurai.uhc.scoreboard.sidebar.SidebarTitleAdapter;
import dev.kurai.uhc.scoreboard.sidebar.adapter.builtin.WaitingSidebarAdapter;
import dev.kurai.uhc.scoreboard.sidebar.listener.SidebarListener;
import dev.kurai.uhc.scoreboard.sidebar.task.updater.SidebarUpdaterTask;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public final class SidebarServiceImpl implements SidebarService {

  private final Map<UUID, Sidebar> sidebars;

  private SidebarTitleAdapter titleAdapter;
  private SidebarAdapter adapter;

  public SidebarServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.sidebars = Maps.newHashMap();
    final var adapter = new WaitingSidebarAdapter(ultraHardcore);
    this.install(adapter, adapter);
    ultraHardcore.eventService().registerListener(new SidebarListener(this));

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(ultraHardcore.plugin(), new SidebarUpdaterTask(this), 0L, 1L);
  }

  @Override
  public @Nullable SidebarTitleAdapter getTitleAdapter() {
    return this.titleAdapter;
  }

  @Override
  public void installTitleAdapter(final @Nullable SidebarTitleAdapter titleAdapter) {
    this.titleAdapter = titleAdapter;
  }

  @Override
  public @Nullable SidebarAdapter getAdapter() {
    return this.adapter;
  }

  @Override
  public void installAdapter(final @Nullable SidebarAdapter adapter) {
    this.adapter = adapter;
  }

  @Override
  public void createScoreboard(final UUID uniqueId) {
    if (this.titleAdapter == null || this.adapter == null || this.sidebars.containsKey(uniqueId)) {
      return;
    }

    final var sidebar = new SidebarImpl(uniqueId);
    this.sidebars.put(uniqueId, sidebar);
    sidebar.send();
  }

  @Override
  public void destroyScoreboard(final UUID uniqueId) {
    if (!this.sidebars.containsKey(uniqueId)) {
      return;
    }

    final var sidebar = this.sidebars.remove(uniqueId);
    sidebar.destroy();
    sidebar.send();
  }

  @Override
  public boolean hasScoreboard(final UUID uniqueId) {
    return this.sidebars.containsKey(uniqueId);
  }

  @Override
  public Sidebar getSidebar(final UUID uniqueId) {
    return this.sidebars.get(uniqueId);
  }
}
