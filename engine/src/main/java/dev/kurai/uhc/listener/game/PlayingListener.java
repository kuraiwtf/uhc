package dev.kurai.uhc.listener.game;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.Style.style;
import static org.bukkit.Material.*;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.game.GameTickEvent;
import dev.kurai.uhc.game.configuration.game.GameConfiguration;
import dev.kurai.uhc.game.configuration.ore.OreConfiguration;
import dev.kurai.uhc.item.builtin.*;
import dev.kurai.uhc.profile.component.InventoryComponent;
import dev.kurai.uhc.profile.component.OfflineActionComponent;
import dev.kurai.uhc.profile.component.ProfileMiningComponent;
import dev.kurai.uhc.util.PlayerUtil;
import java.util.Map;
import java.util.function.Consumer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class PlayingListener implements Listener {

  private final UltraHardcoreAPI ultraHardcore;

  public PlayingListener(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @EventHandler
  public void onJoin(final @NotNull PlayerJoinEvent event) {
    event.setJoinMessage(null);
    this.ultraHardcore
        .getGameService()
        .sendMessage(
            prefix()
                .append(text("Le joueur "))
                .append(text(event.getPlayer().getName(), NamedTextColor.AQUA))
                .append(text(" s'est "))
                .append(text("reconnecté", NamedTextColor.GREEN))
                .append(text('.'))
                .build());
  }

  @EventHandler
  public void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
    event.setQuitMessage(null);
    this.ultraHardcore
        .getGameService()
        .sendMessage(
            prefix()
                .append(text("Le joueur "))
                .append(text(event.getPlayer().getName(), NamedTextColor.AQUA))
                .append(text(" s'est "))
                .append(text("déconnecté", NamedTextColor.RED))
                .append(text(". Il dispose ainsi de "))
                .append(text("15:00", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(text(" pour se reconnecter."))
                .build());
  }

  @EventHandler
  public void processOfflineActions(final GameTickEvent event) {
    for (final var profile :
        this.ultraHardcore
            .getProfileService()
            .getProfiles(profile -> profile.findPlayer().isPresent())) {
      final var offlineActionComponent = profile.getComponent(OfflineActionComponent.class);
      if (offlineActionComponent == null) {
        continue;
      }

      final var poll = offlineActionComponent.getActions().poll();
      if (poll == null) {
        continue;
      }

      final var player = profile.getPlayer();
      poll.onJoin(player);
    }
  }

  @EventHandler
  public void onPlayerDeath(final PlayerDeathEvent event) {
    event.setDeathMessage(null);
    event.setKeepInventory(true);
    event.setKeepLevel(true);

    final var player = event.getEntity();
    final var profile =
        this.ultraHardcore.getProfileService().getOrCreateProfile(player.getUniqueId());
    if (profile == null) {
      return;
    }

    final var inventory = player.getInventory();
    profile.addComponent(
        new InventoryComponent(inventory.getContents(), inventory.getArmorContents()));
    this.ultraHardcore.getGameService().getDeathService().getDeathProcessor().processDeath(event);
  }

  @EventHandler
  public void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof final Player player)
        || !(event.getDamager() instanceof final Arrow arrow)
        || !(arrow.getShooter() instanceof final Player shooter)
        || !GameConfiguration.BOW_HEALTH_VIEW_OPTION.getValue()) {
      return;
    }

    Bukkit.getScheduler()
        .runTaskLaterAsynchronously(
            this.ultraHardcore.getPlugin(),
            () ->
                this.ultraHardcore
                    .getProfileService()
                    .getOrCreateProfile(shooter.getUniqueId())
                    .sendActionBar(
                        PlayerUtil.formatHealthAsHeartBar(
                            player,
                            style(NamedTextColor.DARK_RED),
                            style(NamedTextColor.RED),
                            style(NamedTextColor.YELLOW),
                            style(NamedTextColor.DARK_GRAY))),
            2L);
  }

  private static final Map<@NotNull Material, Consumer<@NotNull ProfileMiningComponent>>
      MINING_STATISTICS = Maps.newHashMap();

  static {
    MINING_STATISTICS.put(
        STONE, component -> component.setStoneMined(component.getStoneMined() + 1));
    MINING_STATISTICS.put(
        IRON_ORE, component -> component.setIronMined(component.getIronMined() + 1));
    MINING_STATISTICS.put(
        GOLD_ORE, component -> component.setGoldMined(component.getGoldMined() + 1));
    MINING_STATISTICS.put(
        DIAMOND_ORE, component -> component.setDiamondMined(component.getDiamondMined() + 1));
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    final var block = event.getBlock();
    if (!MINING_STATISTICS.containsKey(block.getType())) {
      return;
    }

    final var profile =
        this.ultraHardcore.getProfileService().getOrCreateProfile(event.getPlayer().getUniqueId());
    if (profile == null) {
      return;
    }

    final var miningComponent = profile.getComponent(ProfileMiningComponent.class);
    final var blockType = block.getType();

    boolean limitReached = false;
    if (blockType == IRON_ORE) {
      final var ironLimit = OreConfiguration.IRON_LIMIT_OPTION.getValue();
      if (ironLimit > 0 && miningComponent.getIronMined() >= ironLimit) {
        limitReached = true;
        profile.sendMessage(
            prefix()
                .append(text("Vous avez atteint la limite de ", NamedTextColor.RED))
                .append(text("fer", NamedTextColor.GRAY, TextDecoration.BOLD))
                .append(text(" pour cette partie ", NamedTextColor.RED))
                .append(text("(" + ironLimit + ")", NamedTextColor.GOLD))
                .append(text(".", NamedTextColor.RED))
                .build());
      }
    } else if (blockType == GOLD_ORE) {
      final var goldLimit = OreConfiguration.GOLD_LIMIT_OPTION.getValue();
      if (goldLimit > 0 && miningComponent.getGoldMined() >= goldLimit) {
        limitReached = true;
        profile.sendMessage(
            prefix()
                .append(text("Vous avez atteint la limite d'", NamedTextColor.RED))
                .append(text("or", NamedTextColor.YELLOW, TextDecoration.BOLD))
                .append(text(" pour cette partie ", NamedTextColor.RED))
                .append(text("(" + goldLimit + ")", NamedTextColor.GOLD))
                .append(text(".", NamedTextColor.RED))
                .build());
      }
    } else if (blockType == DIAMOND_ORE) {
      final var diamondLimit = OreConfiguration.DIAMOND_LIMIT_OPTION.getValue();
      if (diamondLimit > 0 && miningComponent.getDiamondMined() >= diamondLimit) {
        limitReached = true;
        profile.sendMessage(
            prefix()
                .append(text("Vous avez atteint la limite de ", NamedTextColor.RED))
                .append(text("diamant", NamedTextColor.AQUA, TextDecoration.BOLD))
                .append(text(" pour cette partie ", NamedTextColor.RED))
                .append(text("(" + diamondLimit + ")", NamedTextColor.GOLD))
                .append(text(".", NamedTextColor.RED))
                .build());
      }
    }

    if (limitReached) {
      block.setType(Material.AIR);
      event.setCancelled(true);

      if (blockType == DIAMOND_ORE) {
        event.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 2));
      }
      return;
    }

    MINING_STATISTICS.get(blockType).accept(miningComponent);
  }
}
