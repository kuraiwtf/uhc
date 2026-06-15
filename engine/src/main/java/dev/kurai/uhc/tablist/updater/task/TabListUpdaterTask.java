package dev.kurai.uhc.tablist.updater.task;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import dev.kurai.uhc.tablist.TabListService;
import org.bukkit.Bukkit;

public final class TabListUpdaterTask implements Runnable {

  private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

  private final TabListService tabListService;

  public TabListUpdaterTask(final TabListService tabListService) {
    this.tabListService = tabListService;
  }

  @Override
  public void run() {
    final var headerProvider = this.tabListService.getHeaderProvider();
    final var footerProvider = this.tabListService.getFooterProvider();

    for (final var player : Bukkit.getOnlinePlayers()) {
      PLAYER_MANAGER.sendPacket(
          player,
          new WrapperPlayServerPlayerListHeaderAndFooter(
              headerProvider.provideComponent(player).asComponent(),
              footerProvider.provideComponent(player).asComponent()));
    }
  }
}
