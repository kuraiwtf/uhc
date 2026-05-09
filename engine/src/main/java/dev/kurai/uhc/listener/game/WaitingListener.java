package dev.kurai.uhc.listener.game;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.event.defaults.game.GameStartEvent;
import dev.kurai.uhc.event.service.EventService;
import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.item.builtin.*;
import dev.kurai.uhc.item.service.ItemService;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.profile.service.ProfileService;
import java.util.Map;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WaitingListener implements Listener {

  private static final Map<Integer, Class<? extends CustomItem>> WAITING_ITEMS =
      Map.of(
          0, ScenarioItem.class,
          1, MumbleItem.class,
          7, JumpItem.class,
          8, LogoutItem.class);
  private static final Map<Integer, Class<? extends CustomItem>> HOST_WAITING_ITEMS =
      Map.of(4, ConfigurationItem.class);

  private final BukkitAudiences bukkitAudiences;
  private final EventService eventService;
  private final ItemService itemService;
  private final ModuleService moduleService;
  private final ProfileService profileService;
  private final Plugin plugin;

  private final Location spawnLocation;

  public WaitingListener(
      final BukkitAudiences bukkitAudiences,
      final EventService eventService,
      final ItemService itemService,
      final ModuleService moduleService,
      final ProfileService profileService,
      final Plugin plugin) {
    this.bukkitAudiences = bukkitAudiences;
    this.eventService = eventService;
    this.itemService = itemService;
    this.moduleService = moduleService;
    this.profileService = profileService;
    this.plugin = plugin;

    this.spawnLocation =
        Location.deserialize(plugin.getConfig().getConfigurationSection("spawn").getValues(false));
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    event.setJoinMessage(null);
    final var player = event.getPlayer();
    player.teleport(this.spawnLocation);
    player.setGameMode(GameMode.SURVIVAL);
    player.setFallDistance(0.0f);
    player.setExp(0.0F);
    player.setLevel(0);
    player.setFoodLevel(20);
    player.setSaturation(20f);
    player.setWalkSpeed(0.2F);
    player.setFallDistance(0f);
    player.setMaxHealth(20f);
    player.setHealth(20f);
    player.getInventory().setContents(new ItemStack[36]);
    player.getInventory().setArmorContents(new ItemStack[4]);
    for (final var effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }

    final var profile = this.profileService.createProfile(player);
    this.plugin
        .getLogger()
        .info(
            "Created profile for %s (%s)."
                .formatted(profile.getName(), profile.getId().toString()));

    this.bukkitAudiences
        .all()
        .sendActionBar(
            text()
                .append(text(player.getName(), GREEN))
                .append(text(" a rejoint la partie. "))
                .append(text('(', DARK_GRAY))
                .append(
                    text(player.getServer().getOnlinePlayers().size(), GREEN, TextDecoration.BOLD))
                .append(text('/', DARK_GRAY))
                .append(text(player.getServer().getMaxPlayers(), GREEN))
                .append(text(')', DARK_GRAY))
                .build());

    WAITING_ITEMS.forEach((slot, itemClass) -> this.setItem(player, slot, itemClass));

    if (false) {
      return;
    }

    HOST_WAITING_ITEMS.forEach((slot, itemClass) -> this.setItem(player, slot, itemClass));
  }

  private void setItem(
      final Player player, final int slot, final Class<? extends CustomItem> clazz) {
    this.itemService
        .findByClass(clazz)
        .ifPresent(item -> player.getInventory().setItem(slot, item.provideIcon(player)));
  }

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onFoodLevelChange(final FoodLevelChangeEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerMove(final PlayerMoveEvent event) {
    final var player = event.getPlayer();
    if (player.getLocation().getBlockY() > 160) {
      return;
    }

    player.teleport(this.spawnLocation);
  }

  @EventHandler
  public void onBlockPlace(final BlockPlaceEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerInteract(final PlayerInteractEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onGameStart(final GameStartEvent event) {
    this.eventService.unregisterListener(this);
  }
}
