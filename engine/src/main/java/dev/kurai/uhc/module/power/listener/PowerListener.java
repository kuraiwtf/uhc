package dev.kurai.uhc.module.power.listener;

import static dev.kurai.uhc.util.PlayerUtil.updateHeldItem;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.LeftClickItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.RightClickItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.block.BlockPlacePower;
import dev.kurai.uhc.module.power.defaults.item.impl.parent.AbstractParentItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.impl.LeftClickPlayerTargetItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.impl.RightClickPlayerTargetItemPower;
import dev.kurai.uhc.profile.service.ProfileService;
import dev.kurai.uhc.util.GlobalUtil;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PowerListener extends PacketListenerAbstract implements Listener {

  private final ProfileService profileService;
  private final Plugin plugin;

  public PowerListener(final ProfileService profileService, final Plugin plugin) {
    this.profileService = profileService;
    this.plugin = plugin;
  }

  @EventHandler
  public void onHeldSwap(final PlayerItemHeldEvent event) {
    final var player = event.getPlayer();
    final var profile = this.profileService.getProfile(player.getUniqueId());

    final var item = player.getInventory().getItem(event.getNewSlot());
    if (item.getType().name().contains("SWORD") || item.getType().isBlock()) {
      return;
    }

    final var foundPower =
        profile.getPowers().stream()
            .filter(AbstractItemPower.class::isInstance)
            .map(AbstractItemPower.class::cast)
            .filter(power -> power.provideIcon(player).isSimilar(item))
            .findFirst()
            .orElse(null);

    if (foundPower == null) {
      return;
    }

    Bukkit.getScheduler()
        .runTaskLaterAsynchronously(
            this.plugin,
            () -> {
              final var packet =
                  new PacketPlayOutEntityEquipment(
                      ((CraftPlayer) player).getHandle().getId(),
                      0,
                      new ItemStack(Items.NETHER_STAR));

              for (final var arenaProfile : this.profileService.getProfiles()) {
                if (arenaProfile.getId().equals(player.getUniqueId())) {
                  continue;
                }

                arenaProfile
                    .findPlayer()
                    .ifPresent(
                        target ->
                            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet));
              }
            },
            1L);
  }

  @EventHandler
  public void onInteract(final @NotNull PlayerInteractEvent event) {
    if (!event.hasItem()) {
      return;
    }

    final var player = event.getPlayer();
    final var profile = this.profileService.getProfile(player.getUniqueId());
    if (profile == null) {
      return;
    }

    final var foundPower =
        profile.getPowers().stream()
            .filter(AbstractItemPower.class::isInstance)
            .map(AbstractItemPower.class::cast)
            .filter(power -> power.provideIcon(player).isSimilar(event.getItem()))
            .findFirst()
            .orElse(null);

    if (foundPower == null) {
      return;
    }

    if (event.getAction() == Action.RIGHT_CLICK_AIR
        || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if (foundPower instanceof final RightClickPlayerTargetItemPower targetItemPower) {
        final var target = GlobalUtil.getTargetPlayer(player);
        if (target == null
            || target.getLocation().distanceSquared(player.getLocation())
                > targetItemPower.getRange() * targetItemPower.getRange()) {
          return;
        }

        targetItemPower.setTarget(target);
        targetItemPower.use(player);
        targetItemPower.setTarget(null);
        event.setCancelled(true);
        return;
      } else if (foundPower instanceof RightClickItemPower) {
        foundPower.use(player);
        event.setCancelled(true);
      } else if (foundPower instanceof final AbstractParentItemPower parent) {
        if (parent.getCurrentPower() == null) {
          return;
        }

        parent.getCurrentPower().use(player);
      }
    }

    if (event.getAction() == Action.LEFT_CLICK_AIR
        || event.getAction() == Action.LEFT_CLICK_BLOCK) {
      if (foundPower instanceof final LeftClickPlayerTargetItemPower targetItemPower) {
        final var target = GlobalUtil.getTargetPlayer(player);
        if (target == null
            || target.getLocation().distanceSquared(player.getLocation())
                > targetItemPower.getRange() * targetItemPower.getRange()) {
          return;
        }

        targetItemPower.setTarget(target);
        targetItemPower.use(player);
        targetItemPower.setTarget(null);
        event.setCancelled(true);
        return;
      } else if (foundPower instanceof LeftClickItemPower) {
        foundPower.use(player);
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onBlockPlace(final BlockPlaceEvent event) {
    final var player = event.getPlayer();
    final var profile = this.profileService.getProfile(player.getUniqueId());
    if (profile == null) {
      return;
    }

    final var foundPower =
        profile.getPowers().stream()
            .filter(BlockPlacePower.class::isInstance)
            .map(BlockPlacePower.class::cast)
            .filter(power -> power.provideIcon(player).isSimilar(event.getItemInHand()))
            .findFirst()
            .orElse(null);

    if (foundPower == null) {
      return;
    }

    foundPower.use(player);
    updateHeldItem(player);
  }
}
