package dev.kurai.uhc.listener.game;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameStartEvent;
import dev.kurai.uhc.event.defaults.host.HostAccessUpdateEvent;
import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.item.WaitingItem;
import dev.kurai.uhc.item.builtin.*;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WaitingListener implements Listener {

  private final UltraHardcoreAPI ultraHardcore;

  private final Location spawnLocation;

  public WaitingListener(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    this.spawnLocation =
        Location.deserialize(
            ultraHardcore.plugin().getConfig().getConfigurationSection("spawn").getValues(false));
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

    final var profile = this.ultraHardcore.profileService().getOrCreateProfile(player);
    this.ultraHardcore
        .plugin()
        .getLogger()
        .info(
            "Created profile for %s (%s)."
                .formatted(profile.getName(), profile.getId().toString()));

    this.ultraHardcore
        .bukkitAudiences()
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

    for (final WaitingItem item : this.ultraHardcore.itemService().findWaitingItems()) {
      if (!item.item().isHostOnly()
          || this.ultraHardcore.gameService().hostService().hasHostAccess(player)) {
        this.setItem(player, item.slot(), item.item().getClass());
      }
    }
  }

  @EventHandler
  public void onHostUpdate(final HostAccessUpdateEvent event) {
    final var player = Bukkit.getPlayer(event.getId());
    if (player == null) {
      return;
    }

    for (final WaitingItem item : this.ultraHardcore.itemService().findWaitingItems()) {
      if (!item.item().isHostOnly()) {
        continue;
      }

      final int slot = item.slot();
      if (event.getStatus() == HostAccessUpdateEvent.Status.ALLOWED) {
        this.setItem(player, slot, item.item().getClass());
      } else {
        player.getInventory().setItem(slot, new ItemStack(Material.AIR));
      }
    }
  }

  private void setItem(
      final Player player, final int slot, final Class<? extends CustomItem> clazz) {
    this.ultraHardcore
        .itemService()
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
    if (event.getPlayer().isOp()) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    if (event.getPlayer().isOp()) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerInteract(final PlayerInteractEvent event) {
    if (event.getPlayer().isOp()) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler
  public void onGameStart(final GameStartEvent event) {
    this.ultraHardcore.eventService().unregisterListener(this);
  }
}
