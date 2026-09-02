package dev.kurai.uhc;

import static org.bukkit.Material.*;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import dev.kurai.actionbar.ActionbarService;
import dev.kurai.uhc.command.CommandRegistrar;
import dev.kurai.uhc.command.CommandRegistrarImpl;
import dev.kurai.uhc.command.argument.builtin.uhc.TimerArgumentResolver;
import dev.kurai.uhc.command.defaults.*;
import dev.kurai.uhc.effect.EffectListener;
import dev.kurai.uhc.effect.EffectService;
import dev.kurai.uhc.effect.EffectServiceImpl;
import dev.kurai.uhc.effect.EffectValidatorTask;
import dev.kurai.uhc.event.EventService;
import dev.kurai.uhc.event.EventServiceImpl;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.game.GameServiceImpl;
import dev.kurai.uhc.game.configuration.inventory.InventoryConfiguration;
import dev.kurai.uhc.helpop.HelpOpService;
import dev.kurai.uhc.helpop.HelpOpServiceImpl;
import dev.kurai.uhc.item.ItemService;
import dev.kurai.uhc.item.ItemServiceImpl;
import dev.kurai.uhc.listener.AccessListener;
import dev.kurai.uhc.listener.FixListener;
import dev.kurai.uhc.listener.ItemListener;
import dev.kurai.uhc.listener.game.ResourcePackListener;
import dev.kurai.uhc.listener.game.WaitingListener;
import dev.kurai.uhc.module.ModuleServiceImpl;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.nickname.NicknameService;
import dev.kurai.uhc.nickname.NicknameServiceImpl;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.ProfileServiceImpl;
import dev.kurai.uhc.scoreboard.sidebar.SidebarService;
import dev.kurai.uhc.scoreboard.sidebar.service.SidebarServiceImpl;
import dev.kurai.uhc.skin.SkinService;
import dev.kurai.uhc.skin.SkinServiceImpl;
import dev.kurai.uhc.tablist.TabListService;
import dev.kurai.uhc.tablist.TabListServiceImpl;
import dev.kurai.uhc.tablist.TabListUpdaterTask;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.TimerService;
import dev.kurai.uhc.timer.builtin.BorderTimer;
import dev.kurai.uhc.timer.builtin.InvincibilityTimer;
import dev.kurai.uhc.timer.builtin.PvPTimer;
import dev.kurai.uhc.whitelist.WhitelistService;
import dev.kurai.uhc.whitelist.WhitelistServiceImpl;
import dev.kurai.uhc.win.WinService;
import dev.kurai.uhc.win.WinServiceImpl;
import dev.kurai.uhc.world.WorldService;
import dev.kurai.uhc.world.WorldServiceImpl;
import lombok.Getter;
import net.j4c0b3y.api.menu.MenuHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pt.supercrafting.entity.EntityLib;

@Getter
public final class UltraHardcoreEngine extends UltraHardcoreAPI {

  private BukkitAudiences bukkitAudiences;
  private ActionbarService actionbarService;
  private CommandRegistrar commandRegistrar;
  private EffectService effectService;
  private EntityLib entityLib;
  private EventService eventService;
  private GameService gameService;
  private HelpOpService helpOpService;
  private ItemService itemService;
  private ModuleService moduleService;
  private NicknameService nicknameService;
  private ProfileService profileService;
  private SidebarService sidebarService;
  private SkinService skinService;
  private TabListService tabListService;
  private WhitelistService whitelistService;
  private WinService winService;
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

    this.effectService = new EffectServiceImpl();
    this.entityLib = EntityLib.create(this.plugin, PacketEvents.getAPI());

    this.sidebarService = new SidebarServiceImpl(this);

    this.actionbarService =
        ActionbarService.actionbarService(
            this.plugin, player -> this.bukkitAudiences.player(player));
    this.gameService = new GameServiceImpl(this);
    this.helpOpService = new HelpOpServiceImpl();
    this.itemService = new ItemServiceImpl(this);
    this.moduleService = new ModuleServiceImpl(this);
    this.nicknameService = new NicknameServiceImpl(this.plugin);
    this.profileService = new ProfileServiceImpl(this);
    this.skinService = new SkinServiceImpl(this.plugin, this.profileService);
    this.tabListService = new TabListServiceImpl(this);
    this.whitelistService = new WhitelistServiceImpl();
    this.winService = new WinServiceImpl(this.profileService);
    this.worldService = new WorldServiceImpl(this.plugin, this.profileService);

    this.commandRegistrar.registerCommands(
        new AnswerCommand(this),
        new GroupCommand(this.gameService().groupService(), this.profileService),
        new HelpOpCommand(this),
        new HostCommand(this.bukkitAudiences, this),
        new ModerationCommands(this),
        new ModuleCommands(this),
        new PlayerCommands(this),
        new SpectatorCommand(this),
        new WhitelistCommand(this));

    final TimerService timerService = this.gameService.timerService();
    this.commandRegistrar
        .getArgumentResolverRegistrar()
        .registerArgumentResolver(
            AbstractTimer.class, new TimerArgumentResolver(this.bukkitAudiences, timerService));

    final EventManager eventManager = PacketEvents.getAPI().getEventManager();

    final var fixListener = new FixListener();
    final var resourcePackListener = new ResourcePackListener(this.plugin, this.moduleService);
    eventManager.registerListeners(fixListener, resourcePackListener);

    this.eventService.registerListeners(
        fixListener,
        resourcePackListener,
        new AccessListener(
            this.gameService.hostService(), this.moduleService, this.whitelistService),
        new ItemListener(this.itemService),
        new WaitingListener(this),
        new EffectListener(this.effectService, this.profileService));

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            this.plugin, new TabListUpdaterTask(this.tabListService), 0, 1L);

    InventoryConfiguration.INVENTORY_CONTENT_OPTION.setValue(
        new ItemStack[] {
          new ItemStack(BOOK, 7),
          new ItemStack(WATER_BUCKET),
          new ItemStack(ARROW, 16),
          new ItemStack(GOLDEN_CARROT, 64)
        });

    timerService.registerTimers(
        new InvincibilityTimer(this),
        new PvPTimer(this.worldService),
        new BorderTimer(this.worldService, this.bukkitAudiences));

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            this.plugin, new EffectValidatorTask(this.profileService), 0, 1);
  }
}
