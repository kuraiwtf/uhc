package dev.kurai.uhc.effect;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.effect.event.EffectPostAddEvent;
import dev.kurai.uhc.effect.event.EffectPostRemoveEvent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class EffectListener implements Listener {

  private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

  private final EffectService effectService;
  private final ProfileService profileService;

  public EffectListener(final EffectService effectService, final ProfileService profileService) {
    this.effectService = effectService;
    this.profileService = profileService;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final UUID uniqueId = event.getPlayer().getUniqueId();
    this.validateSpeed(uniqueId);

    for (final EffectType effectType : EffectType.values()) {
      this.updateEffect(uniqueId, effectType);
    }
  }

  @EventHandler
  public void onEffectAdd(final EffectPostAddEvent event) {
    final UUID uniqueId = event.uniqueId();
    final EffectType effectType = event.effect().type();
    if (effectType == EffectType.SPEED) {
      this.validateSpeed(uniqueId);
    }

    this.updateEffect(uniqueId, effectType);
  }

  @EventHandler
  public void onEffectRemove(final EffectPostRemoveEvent event) {
    final UUID uniqueId = event.uniqueId();
    final EffectType effectType = event.effect().type();
    if (effectType == EffectType.SPEED) {
      this.validateSpeed(uniqueId);
    }

    this.updateEffect(uniqueId, effectType);
  }

  @EventHandler
  public void onTeleport(final PlayerTeleportEvent event) {
    if (event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
      return;
    }

    final UUID uniqueId = event.getPlayer().getUniqueId();
    this.validateSpeed(uniqueId);

    for (final EffectType effectType : EffectType.values()) {
      this.updateEffect(uniqueId, effectType);
    }
  }

  @EventHandler
  public void onWorldChange(final PlayerChangedWorldEvent event) {
    final UUID uniqueId = event.getPlayer().getUniqueId();
    this.validateSpeed(uniqueId);

    for (final EffectType effectType : EffectType.values()) {
      this.updateEffect(uniqueId, effectType);
    }
  }

  @EventHandler
  public void onRespawn(final PlayerRespawnEvent event) {
    final UUID uniqueId = event.getPlayer().getUniqueId();
    this.validateSpeed(uniqueId);

    for (final EffectType effectType : EffectType.values()) {
      this.updateEffect(uniqueId, effectType);
    }
  }

  private void updateEffect(final UUID uniqueId, final EffectType effectType) {
    final Player player = Bukkit.getPlayer(uniqueId);
    if (player == null) {
      return;
    }

    final Profile profile = this.profileService.getOrCreateProfile(uniqueId);
    final EffectHolder effectHolder = profile.getComponent(EffectHoldingComponent.class).holder();
    final double valuePerLevel = this.effectService.effectValuePerLevel(effectType) * 100;
    final int amplifier =
        (int) Math.round((effectHolder.getEffectValue(effectType) * 100) / valuePerLevel);

    final WrapperPlayServerRemoveEntityEffect removeEffectPacket =
        new WrapperPlayServerRemoveEntityEffect(player.getEntityId(), effectType.packetType());
    PLAYER_MANAGER.sendPacket(player, removeEffectPacket);

    if (amplifier < 1) {
      return;
    }

    final WrapperPlayServerEntityEffect sendEffectPacket =
        new WrapperPlayServerEntityEffect(
            player.getEntityId(), effectType.packetType(), amplifier - 1, 32767, (byte) 0);
    PLAYER_MANAGER.sendPacket(player, sendEffectPacket);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void applyDamage(final EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof final Player damager)
        || !(event.getEntity() instanceof final Player victim)) {
      return;
    }

    final Profile victimProfile = this.profileService.getOrCreateProfile(victim.getUniqueId());
    final Profile damagerProfile = this.profileService.getOrCreateProfile(damager.getUniqueId());

    final EffectHolder victimEffectHolder =
        victimProfile.getComponent(EffectHoldingComponent.class).holder();
    final EffectHolder damagerEffectHolder =
        damagerProfile.getComponent(EffectHoldingComponent.class).holder();

    event.setDamage(
        this.damageFormula(
            event.getDamage(),
            victimEffectHolder.getEffectValue(EffectType.STRENGTH),
            damagerEffectHolder.getEffectValue(EffectType.RESISTANCE)));
  }

  private double damageFormula(
      final double baseDamage, final double strengthValue, final double resistanceValue) {
    return baseDamage * (1 + strengthValue) * (1 - resistanceValue);
  }

  private void validateSpeed(final UUID uniqueId) {
    final Player player = Bukkit.getPlayer(uniqueId);
    final Profile profile = this.profileService.getOrCreateProfile(uniqueId);
    final EffectHolder effectHolder = profile.getComponent(EffectHoldingComponent.class).holder();
    final double totalPercent = effectHolder.getEffectValue(EffectType.SPEED);
    final float newSpeed = (float) Math.clamp(0.2f * (1 + totalPercent), 0.0, 1.0);
    if (player.getWalkSpeed() != newSpeed) {
      player.setWalkSpeed(newSpeed);
    }
  }
}
