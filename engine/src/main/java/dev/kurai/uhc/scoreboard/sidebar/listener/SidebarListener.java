package dev.kurai.uhc.scoreboard.sidebar.listener;

import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import dev.kurai.uhc.UltraHardcoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class SidebarListener implements Listener {

  private final SidebarService sidebarService;

  public SidebarListener(final SidebarService sidebarService) {
    this.sidebarService = sidebarService;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final var uniqueId = event.getPlayer().getUniqueId();
    Bukkit.getScheduler()
        .runTaskLater(
            UltraHardcoreAPI.getInstance().plugin(),
            () -> this.sidebarService.createScoreboard(uniqueId),
            1L);
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    this.sidebarService.destroyScoreboard(event.getPlayer().getUniqueId());
  }
}
