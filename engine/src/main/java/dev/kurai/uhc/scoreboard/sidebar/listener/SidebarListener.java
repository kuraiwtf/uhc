package dev.kurai.uhc.scoreboard.sidebar.listener;

import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import java.util.concurrent.CompletableFuture;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public final class SidebarListener implements Listener {

  private final SidebarService sidebarService;

  public SidebarListener(final  SidebarService sidebarService) {
    this.sidebarService = sidebarService;
  }

  @EventHandler
  public void onJoin(final  PlayerJoinEvent event) {
    CompletableFuture.runAsync(
        () -> this.sidebarService.createScoreboard(event.getPlayer().getUniqueId()));
  }

  @EventHandler
  public void onQuit(final  PlayerQuitEvent event) {
    CompletableFuture.runAsync(
        () -> this.sidebarService.destroyScoreboard(event.getPlayer().getUniqueId()));
  }
}
