package dev.kurai.uhc.module.power.task.updater;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.glow.GlowModule;
import com.lunarclient.apollo.player.ApolloPlayerManager;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.impl.player.PlayerTargetItemPower;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.state.PlayingProfileState;
import dev.kurai.uhc.util.GlobalUtil;
import java.awt.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GlowingTargetUpdaterTask implements Runnable {

  private static final ApolloPlayerManager PLAYER_MANAGER = Apollo.getPlayerManager();
  private static final GlowModule GLOW_MODULE =
      Apollo.getModuleManager().getModule(GlowModule.class);

  private final ProfileService profileService;

  public GlowingTargetUpdaterTask(final ProfileService profileService) {
    this.profileService = profileService;
  }

  @Override
  public void run() {
    for (final var profile : this.profileService.getPlayingProfiles()) {
      profile
          .findPlayer()
          .ifPresent(
              player -> {
                for (final var power : profile.getPowers()) {
                  if (power instanceof final PlayerTargetItemPower itemPower) {
                    this.handlePower(player, itemPower);
                  }
                }
              });
    }
  }

  private void handlePower(final Player player, final PlayerTargetItemPower power) {
    if (!power.hasPowerInHand(player)) {
      return;
    }

    final var target = GlobalUtil.getTargetPlayer(player);
    if (target == null
        || !target.isOnline()
        || !(this.profileService.getOrCreateProfile(target).getState()
            instanceof PlayingProfileState)) {
      return;
    }

    Apollo.getPlayerManager()
        .getPlayer(player.getUniqueId())
        .ifPresent(
            apolloPlayer -> {
              GLOW_MODULE.overrideGlow(apolloPlayer, target.getUniqueId(), Color.ORANGE);
              Bukkit.getScheduler()
                  .runTaskLater(
                      UltraHardcoreAPI.getInstance().plugin(),
                      () -> {
                        final var newTarget = GlobalUtil.getTargetPlayer(player);
                        if (newTarget == null) {
                          GLOW_MODULE.resetGlow(apolloPlayer, target.getUniqueId());
                          return;
                        }

                        final Profile newProfile =
                            this.profileService.getOrCreateProfile(newTarget);
                        if (!power.hasPowerInHand(player)
                            || !newTarget.getUniqueId().equals(target.getUniqueId())
                            || !(newProfile.getState() instanceof PlayingProfileState)) {
                          GLOW_MODULE.resetGlow(apolloPlayer, target.getUniqueId());
                        }
                      },
                      2L);
            });
  }
}
