package dev.kurai.uhc.module.power.task.updater;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.glow.GlowModule;
import com.lunarclient.apollo.player.ApolloPlayerManager;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.defaults.item.impl.player.PlayerTargetItemPower;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.util.GlobalUtil;
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
    for (final var profile : this.profileService.getProfiles()) {
      profile
          .findPlayer()
          .ifPresent(
              player -> {
                if (PLAYER_MANAGER.hasSupport(player.getUniqueId())) {

                  for (final var power : profile.getPowers()) {
                    if (power instanceof final PlayerTargetItemPower targetPower) {
                      this.processPower(player, targetPower);
                    }
                  }
                }
              });
    }
  }

  private void processPower(final Player player, final PlayerTargetItemPower power) {
    final Player target = GlobalUtil.getTargetPlayer(player);
    if (target == null || player.getLocation().distance(target.getLocation()) > power.getRange()) {
      return;
    }

    PLAYER_MANAGER
        .getPlayer(player.getUniqueId())
        .ifPresent(
            apolloPlayer -> {
              GLOW_MODULE.overrideGlow(
                  apolloPlayer, target.getUniqueId(), power.getColor().asJavaColor());
              Bukkit.getScheduler()
                  .runTaskLaterAsynchronously(
                      UltraHardcoreAPI.getInstance().plugin(),
                      () -> this.validate(player, target),
                      1L);
            });
  }

  private void validate(final Player player, final Player target) {
    final Player potentialTarget = GlobalUtil.getTargetPlayer(player);
    if (potentialTarget == null || target.getUniqueId().equals(potentialTarget.getUniqueId())) {
      return;
    }

    PLAYER_MANAGER
        .getPlayer(player.getUniqueId())
        .ifPresent(apolloPlayer -> GLOW_MODULE.resetGlow(apolloPlayer, target.getUniqueId()));
  }
}
