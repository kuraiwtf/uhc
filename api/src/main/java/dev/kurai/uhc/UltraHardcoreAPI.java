package dev.kurai.uhc;

import com.google.common.base.Preconditions;
import dev.kurai.actionbar.ActionbarService;
import dev.kurai.uhc.command.CommandRegistrar;
import dev.kurai.uhc.effect.EffectService;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.helpop.HelpOpService;
import dev.kurai.uhc.item.ItemService;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.nickname.NicknameService;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import dev.kurai.uhc.skin.SkinService;
import dev.kurai.uhc.tablist.TabListService;
import dev.kurai.uhc.whitelist.WhitelistService;
import dev.kurai.uhc.win.WinService;
import dev.kurai.uhc.world.WorldService;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullUnmarked;
import pt.supercrafting.entity.EntityLib;

@NullUnmarked
public abstract class UltraHardcoreAPI {

  private static UltraHardcoreAPI instance;

  protected final Plugin plugin;

  protected UltraHardcoreAPI(final Plugin plugin) {
    this.plugin = plugin;
  }

  public static synchronized UltraHardcoreAPI create(final UltraHardcoreAPI instance) {
    Preconditions.checkArgument(UltraHardcoreAPI.instance == null, "API instance already exists!");
    return UltraHardcoreAPI.instance = instance;
  }

  public static UltraHardcoreAPI getInstance() {
    return instance;
  }

  public void onLoad() {}

  public void onEnable() {}

  public void onDisable() {}

  public Plugin plugin() {
    return this.plugin;
  }

  public abstract BukkitAudiences bukkitAudiences();

  public abstract ActionbarService actionbarService();

  public abstract CommandRegistrar commandRegistrar();

  public abstract EffectService effectService();

  public abstract EntityLib entityLib();

  public abstract EventService eventService();

  public abstract GameService gameService();

  public abstract HelpOpService helpOpService();

  public abstract ItemService itemService();

  public abstract ModuleService moduleService();

  public abstract NicknameService nicknameService();

  public abstract ProfileService profileService();

  public abstract SidebarService sidebarService();

  public abstract SkinService skinService();

  public abstract TabListService tabListService();

  public abstract WhitelistService whitelistService();

  public abstract WinService winService();

  public abstract WorldService worldService();
}
