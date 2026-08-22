package dev.kurai.uhc.listener.game;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.event.defaults.power.PowerUseEvent;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.item.impl.parent.AbstractParentItemPower;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public final class SpectatorListener implements Listener {

  private static final Class<? extends Component> SPECTATOR_COMPONENT = SpectatorComponent.class;

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
  public void onPowerUse(final PowerUseEvent event) {
    final AbstractPower power = event.getPower();
    if (power instanceof AbstractParentItemPower) {
      return;
    }

    for (final Profile profile :
        this.ultraHardcore
            .profileService()
            .getProfiles(profile -> profile.hasComponent(SPECTATOR_COMPONENT))) {
      profile.sendPrefixedMessage(
          "&6%s&r vient d'utiliser%s &l%s&r."
              .formatted(profile.getName(), power.getColor().asBukkitColor(), power.getName()));
    }
  }
}
