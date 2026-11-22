package dev.kurai.uhc.extension.minigame;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.extension.minigame.command.DuelCommand;
import dev.kurai.uhc.extension.minigame.duel.DuelService;
import dev.kurai.uhc.extension.minigame.listener.DuelListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinigamePlugin extends JavaPlugin {

    private DuelService duelService;

    @Override
    public void onEnable() {
        final UltraHardcoreAPI ultraHardcore = UltraHardcoreAPI.getInstance();
        this.duelService = new DuelService(ultraHardcore.bukkitAudiences());
        ultraHardcore.commandRegistrar().registerCommand(new DuelCommand(this.duelService));
        Bukkit.getPluginManager().registerEvents(new DuelListener(this.duelService), this);
    }
}
