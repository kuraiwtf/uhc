package dev.kurai.uhc.extension.minigame.listener;

import dev.kurai.uhc.extension.minigame.duel.DuelService;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public final class DuelListener implements Listener {

    private final DuelService duelService;

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.duelService.forfeit(event.getPlayer().getUniqueId());
    }
}
