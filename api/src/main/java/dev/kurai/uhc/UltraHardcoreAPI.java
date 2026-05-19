package dev.kurai.uhc;

import com.google.common.base.Preconditions;
import dev.kurai.uhc.actionbar.ActionbarService;
import dev.kurai.uhc.command.CommandRegistrar;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.item.ItemService;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import dev.kurai.uhc.tablist.TabListService;
import dev.kurai.uhc.whitelist.WhitelistService;
import dev.kurai.uhc.world.WorldService;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class UltraHardcoreAPI {

  private static UltraHardcoreAPI instance;

  protected final Plugin plugin;

  protected UltraHardcoreAPI(final @NotNull Plugin plugin) {
    this.plugin = plugin;
  }

  public static synchronized @NotNull UltraHardcoreAPI create(
      final @NotNull UltraHardcoreAPI instance) {
    Preconditions.checkArgument(UltraHardcoreAPI.instance == null, "API instance already exists!");
    return UltraHardcoreAPI.instance = instance;
  }

  public void onLoad() {}

  public void onEnable() {}

  public void onDisable() {}

  public static @NotNull UltraHardcoreAPI getInstance() {
    return instance;
  }

  public @NotNull Plugin getPlugin() {
    return this.plugin;
  }

  public abstract @NotNull BukkitAudiences getBukkitAudiences();

  public abstract @NotNull ActionbarService getActionbarService();

  public abstract @NotNull CommandRegistrar getCommandRegistrar();

  public abstract @NotNull EventService getEventService();

  public abstract @NotNull GameService getGameService();

  public abstract @NotNull ItemService getItemService();

  public abstract @NotNull ModuleService getModuleService();

  public abstract @NotNull ProfileService getProfileService();

  public abstract @NotNull SidebarService getSidebarService();

  public abstract @NotNull TabListService getTabListService();

  public abstract @NotNull WhitelistService getWhitelistService();

  public abstract @NotNull WorldService getWorldService();
}
