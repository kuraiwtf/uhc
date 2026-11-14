package dev.kurai.uhc.effect;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import dev.kurai.uhc.effect.component.EffectHoldingComponent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import org.bukkit.entity.Player;

public final class EffectPacketSender {

  public static final int CLIENT_DURATION = 32767;

  private static final PlayerManager PLAYER_MANAGER = PacketEvents.getAPI().getPlayerManager();

  private final EffectService effectService;
  private final ProfileService profileService;

  public EffectPacketSender(final EffectService effectService, final ProfileService profileService) {
    this.effectService = effectService;
    this.profileService = profileService;
  }

  public void send(final Player player, final EffectType effectType, final boolean reset) {
    final Profile profile = this.profileService.getOrCreateProfile(player.getUniqueId());
    final EffectHolder effectHolder = profile.getComponent(EffectHoldingComponent.class).holder();
    final double valuePerLevel = this.effectService.effectValuePerLevel(effectType) * 100;
    final int amplifier =
        (int) Math.floor((effectHolder.getEffectValue(effectType) * 100) / valuePerLevel + 1e-9);

    if (reset) {
      PLAYER_MANAGER.sendPacket(
          player,
          new WrapperPlayServerRemoveEntityEffect(player.getEntityId(), effectType.packetType()));
    }

    if (amplifier < 1) {
      return;
    }

    PLAYER_MANAGER.sendPacket(
        player,
        new WrapperPlayServerEntityEffect(
            player.getEntityId(), effectType.packetType(), amplifier - 1, CLIENT_DURATION, (byte) 0));
  }
}
