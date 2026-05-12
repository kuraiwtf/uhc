package dev.kurai.uhc;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kurai.uhc.actionbar.service.ActionbarService;
import dev.kurai.uhc.actionbar.service.ActionbarServiceImpl;
import dev.kurai.uhc.actionbar.task.updater.ActionbarUpdaterTask;
import dev.kurai.uhc.command.argument.builtin.uhc.TimerArgumentResolver;
import dev.kurai.uhc.command.defaults.*;
import dev.kurai.uhc.command.registrar.CommandRegistrar;
import dev.kurai.uhc.command.registrar.CommandRegistrarImpl;
import dev.kurai.uhc.event.service.EventService;
import dev.kurai.uhc.event.service.EventServiceImpl;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.GameServiceImpl;
import dev.kurai.uhc.item.service.ItemService;
import dev.kurai.uhc.item.service.ItemServiceImpl;
import dev.kurai.uhc.listener.FixListener;
import dev.kurai.uhc.listener.ItemListener;
import dev.kurai.uhc.listener.game.WaitingListener;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.module.service.ModuleServiceImpl;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.ProfileServiceImpl;
import dev.kurai.uhc.scoreboard.sidebar.service.SidebarService;
import dev.kurai.uhc.scoreboard.sidebar.service.SidebarServiceImpl;
import dev.kurai.uhc.tablist.service.TabListService;
import dev.kurai.uhc.tablist.service.TabListServiceImpl;
import dev.kurai.uhc.tablist.updater.task.TabListUpdaterTask;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.whitelist.service.WhitelistService;
import dev.kurai.uhc.whitelist.service.WhitelistServiceImpl;
import dev.kurai.uhc.world.service.WorldService;
import dev.kurai.uhc.world.service.WorldServiceImpl;
import net.j4c0b3y.api.menu.MenuHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class UltraHardcoreEngine extends UltraHardcoreAPI {

  private BukkitAudiences bukkitAudiences;
  private ActionbarService actionbarService;
  private CommandRegistrar commandRegistrar;
  private EventService eventService;
  private GameService gameService;
  private ItemService itemService;
  private ModuleService moduleService;
  private ProfileService profileService;
  private SidebarService sidebarService;
  private TabListService tabListService;
  private WhitelistService whitelistService;
  private WorldService worldService;

  public UltraHardcoreEngine(final @NotNull Plugin plugin) {
    super(plugin);
  }

  @Override
  public void onLoad() {
    this.plugin.saveDefaultConfig();
  }

  @Override
  public void onEnable() {
    this.bukkitAudiences = BukkitAudiences.create(this.plugin);
    new MenuHandler((JavaPlugin) this.plugin);
    this.commandRegistrar = new CommandRegistrarImpl(this);
    this.eventService = new EventServiceImpl(this.plugin);

    this.worldService = new WorldServiceImpl(this.plugin, this.bukkitAudiences);
    this.sidebarService = new SidebarServiceImpl(this);

    this.actionbarService = new ActionbarServiceImpl();
    this.gameService = new GameServiceImpl(this);
    this.itemService = new ItemServiceImpl(this);
    this.moduleService = new ModuleServiceImpl(this);
    this.profileService = new ProfileServiceImpl(this);
    this.tabListService = new TabListServiceImpl();
    this.whitelistService = new WhitelistServiceImpl();

    this.commandRegistrar.registerCommands(
        new DeveloperCommand(this),
        new PlayerCommands(this),
        new ModerationCommands(this),
        new HostCommand(this),
        new WhitelistCommand(this.bukkitAudiences, this.whitelistService));

    this.commandRegistrar
        .getArgumentResolverRegistrar()
        .registerArgumentResolver(
            AbstractTimer.class,
            new TimerArgumentResolver(this.bukkitAudiences, this.gameService.getTimerService()));

    final var fixListener = new FixListener();
    PacketEvents.getAPI().getEventManager().registerListener(fixListener);

    this.eventService.registerListeners(
        fixListener,
        new ItemListener(this.itemService),
        new WaitingListener(
            this.bukkitAudiences,
            this.eventService,
            this.itemService,
            this.moduleService,
            this.profileService,
            this.plugin));

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            this.plugin,
            new ActionbarUpdaterTask(this.bukkitAudiences, this.actionbarService),
            0,
            1L);

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            this.plugin, new TabListUpdaterTask(this.tabListService), 0, 1L);
  }

  @Override
  public @NotNull BukkitAudiences getBukkitAudiences() {
    return this.bukkitAudiences;
  }

  @Override
  public @NotNull ActionbarService getActionbarService() {
    return this.actionbarService;
  }

  @Override
  public @NotNull CommandRegistrar getCommandRegistrar() {
    return this.commandRegistrar;
  }

  @Override
  public @NotNull EventService getEventService() {
    return this.eventService;
  }

  @Override
  public @NotNull GameService getGameService() {
    return this.gameService;
  }

  @Override
  public @NotNull ItemService getItemService() {
    return this.itemService;
  }

  @Override
  public @NotNull ModuleService getModuleService() {
    return this.moduleService;
  }

  @Override
  public @NotNull ProfileService getProfileService() {
    return this.profileService;
  }

  @Override
  public @NotNull SidebarService getSidebarService() {
    return this.sidebarService;
  }

  @Override
  public @NotNull TabListService getTabListService() {
    return this.tabListService;
  }

  @Override
  public @NotNull WhitelistService getWhitelistService() {
    return this.whitelistService;
  }

  @Override
  public @NotNull WorldService getWorldService() {
    return this.worldService;
  }
}
