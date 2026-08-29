package dev.kurai.uhc.tablist;

import static dev.kurai.uhc.tablist.TabListPart.Position.BOTTOM;
import static dev.kurai.uhc.tablist.TabListPart.Position.TOP;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

public final class TabListUpdaterTask implements Runnable {

  private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

  private final TabListService tabListService;

  public TabListUpdaterTask(final TabListService tabListService) {
    this.tabListService = tabListService;
  }

  @Override
  public void run() {
    for (final var player : Bukkit.getOnlinePlayers()) {
      PLAYER_MANAGER.sendPacket(
          player,
          new WrapperPlayServerPlayerListHeaderAndFooter(
              this.getHeader(player), this.getFooter(player)));
    }
  }

  @Contract(pure = true)
  private Component getHeader(final Player player) {
    final TextComponent.Builder header = Component.text().appendNewline();

    for (final TabListPart part : this.tabListService.partsByPosition(TOP)) {
      header.append(part.render(player)).appendNewline();
    }

    return header.build();
  }

  @Contract(pure = true)
  private Component getFooter(final Player player) {
    final TextComponent.Builder footer = Component.text().appendNewline();

    for (final TabListPart part : this.tabListService.partsByPosition(BOTTOM)) {
      footer.append(part.render(player)).appendNewline();
    }

    return footer.build();
  }
}
