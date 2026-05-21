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

  public @NotNull Plugin plugin() {
    return this.plugin;
  }

  public abstract @NotNull BukkitAudiences bukkitAudiences();

  public abstract @NotNull ActionbarService actionbarService();

  public abstract @NotNull CommandRegistrar commandRegistrar();

  public abstract @NotNull EventService eventService();

  public abstract @NotNull GameService gameService();

  public abstract @NotNull ItemService itemService();

  public abstract @NotNull ModuleService moduleService();

  public abstract @NotNull ProfileService profileService();

  public abstract @NotNull SidebarService sidebarService();

  public abstract @NotNull TabListService tabListService();

  public abstract @NotNull WhitelistService whitelistService();

  public abstract @NotNull WorldService worldService();
}
