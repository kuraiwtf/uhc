package dev.kurai.uhc;

import com.github.retrooper.packetevents.PacketEvents;
import dev.kurai.uhc.actionbar.ActionbarService;
import dev.kurai.uhc.actionbar.ActionbarServiceImpl;
import dev.kurai.uhc.actionbar.ActionbarUpdaterTask;
import dev.kurai.uhc.command.CommandRegistrar;
import dev.kurai.uhc.command.CommandRegistrarImpl;
import dev.kurai.uhc.command.argument.builtin.uhc.TimerArgumentResolver;
import dev.kurai.uhc.command.defaults.*;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.event.EventServiceImpl;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.GameServiceImpl;
import dev.kurai.uhc.item.ItemService;
import dev.kurai.uhc.item.ItemServiceImpl;
import dev.kurai.uhc.listener.FixListener;
import dev.kurai.uhc.listener.ItemListener;
import dev.kurai.uhc.listener.game.WaitingListener;
import dev.kurai.uhc.module.ModuleServiceImpl;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.ProfileServiceImpl;
import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import dev.kurai.uhc.scoreboard.sidebar.service.SidebarServiceImpl;
import dev.kurai.uhc.tablist.TabListService;
import dev.kurai.uhc.tablist.service.TabListServiceImpl;
import dev.kurai.uhc.tablist.updater.task.TabListUpdaterTask;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.whitelist.WhitelistService;
import dev.kurai.uhc.whitelist.WhitelistServiceImpl;
import dev.kurai.uhc.world.WorldService;
import dev.kurai.uhc.world.WorldServiceImpl;
import lombok.Getter;
import net.j4c0b3y.api.menu.MenuHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pt.supercrafting.entity.EntityLib;

@Getter
public final class UltraHardcoreEngine extends UltraHardcoreAPI {

  private BukkitAudiences bukkitAudiences;
  private ActionbarService actionbarService;
  private CommandRegistrar commandRegistrar;
  private EntityLib entityLib;
  private EventService eventService;
  private GameService gameService;
  private ItemService itemService;
  private ModuleService moduleService;
  private ProfileService profileService;
  private SidebarService sidebarService;
  private TabListService tabListService;
  private WhitelistService whitelistService;
  private WorldService worldService;

  public UltraHardcoreEngine(final Plugin plugin) {
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

    this.entityLib = EntityLib.create(this.plugin, PacketEvents.getAPI());

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
        new PlayerCommands(this),
        new ModuleCommands(this),
        new ModerationCommands(this),
        new HostCommand(this.bukkitAudiences, this),
        new WhitelistCommand(this.bukkitAudiences, this.whitelistService));

    this.commandRegistrar
        .getArgumentResolverRegistrar()
        .registerArgumentResolver(
            AbstractTimer.class,
            new TimerArgumentResolver(this.bukkitAudiences, this.gameService.timerService()));

    final var fixListener = new FixListener();
    PacketEvents.getAPI().getEventManager().registerListener(fixListener);

    this.eventService.registerListeners(
        fixListener, new ItemListener(this.itemService), new WaitingListener(this));

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
}
