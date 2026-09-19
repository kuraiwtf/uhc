package dev.kurai.uhc.module.power.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import com.google.common.collect.Lists;
import dev.kurai.uhc.event.defaults.power.PowerTargetSelectionEvent;
import dev.kurai.uhc.event.defaults.power.cooldown.PowerCooldownEndEvent;
import dev.kurai.uhc.event.defaults.power.cooldown.PowerCooldownStartEvent;
import dev.kurai.uhc.module.power.defaults.command.AbstractCommandPower;
import dev.kurai.uhc.module.power.defaults.command.argument.PowerArgument;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.LeftClickItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.RightClickItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.block.BlockPlacePower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.PlayerTargetItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.impl.LeftClickPlayerTargetItemPower;
import dev.kurai.uhc.module.power.defaults.item.impl.player.impl.RightClickPlayerTargetItemPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.GlobalUtil;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
  public void onCooldownStart(final PowerCooldownStartEvent event) {
    this.updatePower(event.player(), event.restriction());
  }

  @EventHandler
  public void onCooldownEnd(final PowerCooldownEndEvent event) {
    this.updatePower(event.player(), event.restriction());
  }

  private void updatePower(
      final @Nullable Player player, final CooldownPowerRestriction restriction) {
    if (player == null) {
      return;
    }

    final Profile profile = this.profileService.getOrCreateProfile(player);
    if (!(profile.getPower(restriction.getId()) instanceof final AbstractItemPower power)) {
      return;
    }

    final PlayerInventory inventory = player.getInventory();
    for (final ItemStack content : inventory.getContents()) {
      if (!power.isSimilar(content)) {
        continue;
      }

      inventory.setItem(inventory.first(content), power.getIcon(player));
    }
  }

  @Override
  public void onPacketSend(final PacketSendEvent event) {
    if (event.getPacketType() != PacketType.Play.Server.ENTITY_EQUIPMENT) {
      return;
    }

    final WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(event);
    final Equipment hand =
        packet.getEquipment().stream()
            .filter(equipment -> equipment.getSlot() == EquipmentSlot.MAIN_HAND)
            .findFirst()
            .orElse(null);

    if (hand == null) {
      return;
    }

    final Player player = event.getPlayer();
    if (packet.getEntityId() == player.getEntityId()) {
      return;
    }

    final Profile profile = this.profileService.getOrCreateProfile(player);
    final var foundPower =
        profile.getPowers().stream()
            .filter(AbstractItemPower.class::isInstance)
            .map(AbstractItemPower.class::cast)
            .filter(
                power ->
                    power
                        .getIcon(player)
                        .isSimilar(SpigotConversionUtil.toBukkitItemStack(hand.getItem())))
            .findFirst()
            .orElse(null);

    if (foundPower == null) {
      return;
    }

    hand.setItem(
        com.github.retrooper.packetevents.protocol.item.ItemStack.builder()
            .type(ItemTypes.NETHER_STAR)
            .build());

    packet.setEquipment(packet.getEquipment());
    event.markForReEncode(true);
  }

  @EventHandler
  public void onPlayerDropItem(final PlayerDropItemEvent event) {
    final Player player = event.getPlayer();
    final Profile profile = this.profileService.getOrCreateProfile(player);
    profile.getPowers().stream()
        .filter(AbstractItemPower.class::isInstance)
        .map(AbstractItemPower.class::cast)
        .filter(power -> power.getIcon(player).isSimilar(event.getItemDrop().getItemStack()))
        .findFirst()
        .ifPresent(_ -> event.setCancelled(true));
  }

  @EventHandler
  public void onInteract(final PlayerInteractEvent event) {
    if (!event.hasItem()) {
      return;
    }

    final var player = event.getPlayer();
    final var profile = this.profileService.getOrCreateProfile(player.getUniqueId());

    final boolean rightClick = event.getAction().name().contains("RIGHT");
    final boolean leftClick = event.getAction().name().contains("LEFT");
    if (!rightClick && !leftClick) {
      return;
    }

    final var simplePowerClass = rightClick ? RightClickItemPower.class : LeftClickItemPower.class;
    final var foundPower =
        profile.getPowers().stream()
            .filter(simplePowerClass::isInstance)
            .map(simplePowerClass::cast)
            .filter(power -> power.hasPowerInHand(player))
            .findFirst()
            .orElse(null);

    if (foundPower == null) {
      final var targetPowerClass =
          rightClick ? RightClickPlayerTargetItemPower.class : LeftClickPlayerTargetItemPower.class;
      profile.getPowers().stream()
          .filter(targetPowerClass::isInstance)
          .map(targetPowerClass::cast)
          .filter(power -> power.hasPowerInHand(player))
          .findFirst()
          .ifPresent(
              foundTargetPower -> this.handleTargetItemPower(player, foundTargetPower, event));
      return;
    }

    foundPower.use(player);
    event.setCancelled(true);
  }

  /*@EventHandler
  public void onParentInteract(final PlayerInteractEvent event) {
    if (!event.hasItem()) {
      return;
    }

    final var player = event.getPlayer();
    final var profile = this.profileService.getOrCreateProfile(player.getUniqueId());
    if (profile == null) {
      return;
    }

    if (!event.getAction().name().contains("RIGHT")) {
      return;
    }

    final var foundPower =
        profile.getPowers().stream()
            .filter(AbstractParentItemPower.class::isInstance)
            .map(AbstractParentItemPower.class::cast)
            .filter(power -> power.getIcon(player).isSimilar(event.getItem()))
            .findFirst()
            .orElse(null);

    if (foundPower == null || foundPower.getCurrentPower() == null) {
      return;
    }

    foundPower.getCurrentPower().use(player);
    event.setCancelled(true);
  }*/

  private void handleTargetItemPower(
      final Player player, final PlayerTargetItemPower foundPower, final Cancellable cancellable) {
    final var target = GlobalUtil.getTargetPlayer(player);
    if (target == null
        || target.getLocation().distanceSquared(player.getLocation())
            > foundPower.getRange() * foundPower.getRange()) {
      player.sendMessage(CC.prefix("&cVous devez cibler un joueur pour utiliser ce pouvoir."));
      return;
    }

    final PowerTargetSelectionEvent event =
        new PowerTargetSelectionEvent(
            this.profileService.getOrCreateProfile(player),
            this.profileService.getOrCreateProfile(target),
            foundPower);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      player.sendMessage(CC.prefix("§cVous ne pouvez pas cibler ce joueur."));
      return;
    }

    foundPower.setTarget(target);
    foundPower.use(player);
    foundPower.setTarget(null);
    cancellable.setCancelled(true);
  }

  @EventHandler
  public void onBlockPlace(final BlockPlaceEvent event) {
    final var player = event.getPlayer();
    final var profile = this.profileService.getOrCreateProfile(player.getUniqueId());

    final var foundPower =
        profile.getPowers().stream()
            .filter(AbstractItemPower.class::isInstance)
            .map(AbstractItemPower.class::cast)
            .filter(power -> power.hasPowerInHand(player))
            .findFirst()
            .orElse(null);

    if (foundPower == null) {
      return;
    }

    event.setCancelled(true);

    if (foundPower instanceof BlockPlacePower) {
      foundPower.use(player);
    }
  }

  @EventHandler
  public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
    final var player = event.getPlayer();
    final var profile = this.profileService.getOrCreateProfile(player.getUniqueId());
    if (profile == null) {
      return;
    }

    final var prefix = "/" + this.moduleService.getCurrentModule().getCommandName() + " ";
    if (!event.getMessage().startsWith(prefix)) {
      return;
    }

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
