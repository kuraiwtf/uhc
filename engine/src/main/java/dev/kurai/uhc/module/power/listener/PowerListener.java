package dev.kurai.uhc.module.power.listener;

import static dev.kurai.uhc.util.PlayerUtil.updateHeldItem;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.google.common.collect.Lists;
import dev.kurai.uhc.module.power.defaults.command.AbstractCommandPower;
import dev.kurai.uhc.module.power.defaults.command.argument.PowerArgument;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.LeftClickItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.RightClickItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.block.BlockPlacePower;
import dev.kurai.uhc.module.power.defaults.item.impl.parent.AbstractParentItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.PlayerTargetItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.impl.LeftClickPlayerTargetItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.impl.RightClickPlayerTargetItemPower;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.profile.service.ProfileService;
import dev.kurai.uhc.util.GlobalUtil;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PowerListener extends PacketListenerAbstract implements Listener {

  private final ProfileService profileService;
  private final ModuleService moduleService;
  private final Plugin plugin;

  public PowerListener(
      final ProfileService profileService, final ModuleService moduleService, final Plugin plugin) {
    this.profileService = profileService;
    this.moduleService = moduleService;
    this.plugin = plugin;
  }

  @EventHandler
  public void onHeldSwap(final PlayerItemHeldEvent event) {
    final var player = event.getPlayer();
    final var profile = this.profileService.getProfile(player.getUniqueId());

    final var item = player.getInventory().getItem(event.getNewSlot());
    if (item == null || item.getType().name().contains("SWORD") || item.getType().isBlock()) {
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
  public void onInteract(final PlayerInteractEvent event) {
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
        this.handleTargetItemPower(player, targetItemPower, event);
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
        this.handleTargetItemPower(player, targetItemPower, event);
      } else if (foundPower instanceof LeftClickItemPower) {
        foundPower.use(player);
        event.setCancelled(true);
      }
    }
  }

  private void handleTargetItemPower(
      final Player player, final PlayerTargetItemPower power, final Cancellable cancellable) {
    final var target = GlobalUtil.getTargetPlayer(player);
    if (target == null
        || target.getLocation().distanceSquared(player.getLocation())
            > power.getRange() * power.getRange()) {
      return;
    }

    power.setTarget(target);
    power.use(player);
    power.setTarget(null);
    cancellable.setCancelled(true);
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

  @EventHandler
  public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
    final var player = event.getPlayer();
    final var profile = this.profileService.getProfile(player.getUniqueId());
    if (profile == null) {
      return;
    }

    final var prefix = "/" + this.moduleService.getCurrentModule().getCommandName() + " ";
    final var arguments =
        Lists.newArrayList(event.getMessage().substring(prefix.length()).split(" "));
    final var commandName = arguments.removeFirst();

    final var foundPower =
        profile.getPowers().stream()
            .filter(AbstractCommandPower.class::isInstance)
            .map(AbstractCommandPower.class::cast)
            .filter(power -> commandName.equalsIgnoreCase(power.getCommandName()))
            .findFirst()
            .orElse(null);

    if (foundPower == null) {
      return;
    }

    final var powerArguments = Lists.<PowerArgument>newArrayList();
    for (final var argument : arguments) {
      powerArguments.add(new PowerArgument("argument_" + powerArguments.size(), argument));
    }

    foundPower.setArguments(powerArguments.toArray(new PowerArgument[0]));
    foundPower.use(player);
    foundPower.setArguments(null);
    event.setCancelled(true);
  }
}
