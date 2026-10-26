package dev.kurai.uhc.listener.game;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.event.defaults.power.PowerUseEvent;
import dev.kurai.uhc.logger.LogCategories;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.item.impl.parent.AbstractParentItemPower;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public final class SpectatorListener implements Listener {

  private static final Class<? extends Component> SPECTATOR_COMPONENT = SpectatorComponent.class;

  private static final Set<String> PRIVATE_COMMANDS =
      Set.of(
          "msg", "tell", "w", "whisper", "r", "reply", "m", "t", "helpop", "hop", "login",
          "l", "register", "reg", "password");

  private final UltraHardcoreAPI ultraHardcore;

  @EventHandler
  public void onEntityDamage(final EntityDamageEvent event) {
    final Entity entity = event.getEntity();
    if (entity.getType() != EntityType.PLAYER) {
      return;
    }

    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(entity.getUniqueId());
    if (profile.hasComponent(SPECTATOR_COMPONENT)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
    final Entity entity = event.getDamager();
    if (entity.getType() != EntityType.PLAYER) {
      return;
    }
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(entity.getUniqueId());
    if (profile.hasComponent(SPECTATOR_COMPONENT)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerInteract(final PlayerInteractEvent event) {
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(event.getPlayer().getUniqueId());
    if (profile.hasComponent(SPECTATOR_COMPONENT)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockBreak(final BlockBreakEvent event) {
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(event.getPlayer().getUniqueId());
    if (profile.hasComponent(SPECTATOR_COMPONENT)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onBlockPlace(final BlockPlaceEvent event) {
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(event.getPlayer().getUniqueId());
    if (profile.hasComponent(SPECTATOR_COMPONENT)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onCommand(final PlayerCommandPreprocessEvent event) {
    final Player player = event.getPlayer();
    final Profile profile =
        this.ultraHardcore.profileService().getOrCreateProfile(player.getUniqueId());
    if (profile.hasComponent(SPECTATOR_COMPONENT)) {
      return;
    }

    final String content = event.getMessage();
    if (content.startsWith("/") && !isPrivateCommand(content)) {
      this.ultraHardcore
          .loggerService()
          .broadcastSpectator(
              LogCategories.COMMAND,
              "§7§o" + player.getName() + ": " + content,
              player.getLocation());
    }
  }

  private static boolean isPrivateCommand(final String content) {
    final String label = content.substring(1).split(" ", 2)[0].toLowerCase();
    return PRIVATE_COMMANDS.contains(label);
  }

  @EventHandler
  public void onPowerUse(final PowerUseEvent event) {
    final AbstractPower power = event.getPower();
    final Player holder = event.getProfile().getPlayer();
    if (power instanceof AbstractParentItemPower || holder == null) {
      return;
    }

    this.ultraHardcore
        .loggerService()
        .broadcastSpectator(
            LogCategories.POWER,
            "§6"
                + event.getProfile().getName()
                + "§f vient d'utiliser§6 "
                + power.getName()
                + "§f.",
            holder.getLocation());
  }
}
