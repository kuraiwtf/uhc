package dev.kurai.uhc.effect;

import static org.bukkit.event.entity.EntityDamageEvent.DamageModifier.RESISTANCE;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.effect.event.EffectPostAddEvent;
import dev.kurai.uhc.effect.event.EffectPostRemoveEvent;
import dev.kurai.uhc.effect.event.ResistanceApplyEvent;
import dev.kurai.uhc.effect.event.StrengthApplyEvent;
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
  private static final double EPSILON = 1e-9;

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
  public void onStrength(final EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof final Player damager)
        || !(event.getEntity() instanceof final Player victim)) {
      return;
    }

    final Profile profile = this.profileService.getOrCreateProfile(damager.getUniqueId());
    final EffectHolder effectHolder = profile.getComponent(EffectHoldingComponent.class).holder();
    final double strengthPercent = effectHolder.getEffectValue(EffectType.STRENGTH);
    if (strengthPercent == 0.0) {
      return;
    }

    final StrengthApplyEvent applyEvent = new StrengthApplyEvent(damager, victim, strengthPercent);
    Bukkit.getPluginManager().callEvent(applyEvent);
    if (applyEvent.isCancelled()) {
      return;
    }

    final double multiplier = Math.max(0.0, 1 + applyEvent.strengthValue());
    event.setDamage(event.getDamage() * multiplier);
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onResistance(final EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof final Player damager)
        || !(event.getEntity() instanceof final Player victim)) {
      return;
    }

    final Profile profile = this.profileService.getOrCreateProfile(victim.getUniqueId());
    final EffectHolder effectHolder = profile.getComponent(EffectHoldingComponent.class).holder();
    final double resistancePercent = effectHolder.getEffectValue(EffectType.RESISTANCE);
    if (resistancePercent == 0.0) {
      return;
    }

    final ResistanceApplyEvent applyEvent =
        new ResistanceApplyEvent(damager, victim, resistancePercent);
    Bukkit.getPluginManager().callEvent(applyEvent);
    if (applyEvent.isCancelled()) {
      return;
    }

    final double currentDamage = event.getFinalDamage();
    final double reduction = currentDamage * Math.min(applyEvent.resistanceValue(), 1.0);
    event.setDamage(RESISTANCE, -reduction);
  }

  private void validateSpeed(final UUID uniqueId) {
    final Player player = Bukkit.getPlayer(uniqueId);
    final Profile profile = this.profileService.getOrCreateProfile(uniqueId);
    final EffectHolder effectHolder = profile.getComponent(EffectHoldingComponent.class).holder();
    final double totalPercent = effectHolder.getEffectValue(EffectType.SPEED);
    final float newSpeed = (float) Math.clamp(0.2f * (1 + totalPercent), 0.0, 1.0);
    player.setWalkSpeed(newSpeed);
  }
}
