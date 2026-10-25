package dev.kurai.uhc.util;

import static java.util.Objects.requireNonNull;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.camp.Camp;
import dev.kurai.uhc.module.camp.CampComponent;
import dev.kurai.uhc.module.role.RoleComponent;
import dev.kurai.uhc.module.role.RoleMeta;
import dev.kurai.uhc.profile.Profile;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public final class RoleUtils {

  private RoleUtils() {}

  private static boolean isSameRole(final RoleMeta a, final RoleMeta b) {
    return requireNonNull(a).equals(requireNonNull(b));
  }

  public static List<Profile> getPlayersInCamp(final Camp camp) {
    final List<Profile> list = new ArrayList<>();

    for (final Profile profile :
        UltraHardcoreAPI.getInstance().profileService().getPlayingProfiles()) {
      if (profile.hasComponent(CampComponent.class)
          && requireNonNull(profile.getComponent(CampComponent.class)).camp().equals(camp)) {
        list.add(profile);
      }
    }

    return list;
  }

  public static boolean hasRole(final Profile profile, final RoleMeta info) {
    return profile.hasComponent(RoleComponent.class)
        && isSameRole(
            requireNonNull(profile.getComponent(RoleComponent.class)).role().roleMeta(), info);
  }

  public static boolean hasRole(final Player player, final RoleMeta info) {
    Profile profile = UltraHardcoreAPI.getInstance().profileService().getOrCreateProfile(player);
    return hasRole(profile, info);
  }

  public static List<Profile> findPlayersWithRole(final RoleMeta info) {
    final List<Profile> list = new ArrayList<>();

    for (final Profile profile :
        UltraHardcoreAPI.getInstance().profileService().getPlayingProfiles()) {
      if (profile.hasComponent(RoleComponent.class)
          && isSameRole(
              requireNonNull(profile.getComponent(RoleComponent.class)).role().roleMeta(), info)) {
        list.add(profile);
      }
    }

    return list;
  }

  public static @Nullable Profile findPlayerWithRole(final RoleMeta info) {
    for (final Profile profile :
        UltraHardcoreAPI.getInstance().profileService().getPlayingProfiles()) {
      if (profile.hasComponent(RoleComponent.class)
          && isSameRole(
              requireNonNull(profile.getComponent(RoleComponent.class)).role().roleMeta(), info)) {
        return profile;
      }
    }

    return null;
  }
}
