package dev.kurai.uhc.nickname;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

final class NicknameListener implements Listener {

  private final NicknameService nicknameService;

  NicknameListener(final NicknameService nicknameService) {
    this.nicknameService = nicknameService;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final NicknameHolder holder = this.nicknameService.holder(player.getUniqueId());
    if (holder instanceof final NicknameHolderImpl impl) {
      impl.resetKnownTeams();
    }

    for (final Player receiver : Bukkit.getOnlinePlayers()) {
      final NicknameHolder receiverHolder = this.nicknameService.holder(receiver.getUniqueId());
      receiverHolder.updateNickname(player.getUniqueId());
      holder.updateNickname(receiver.getUniqueId());
    }
  }
}
