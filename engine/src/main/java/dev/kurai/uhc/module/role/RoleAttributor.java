package dev.kurai.uhc.module.role;

import static java.time.Duration.*;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.title.Title.Times.times;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.camp.CampComponent;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.Color;
import dev.kurai.uhc.util.RoleUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class RoleAttributor {

  private static final long DISTRIBUTE_DELAY_TICKS = 10L;

  private final UltraHardcoreAPI ultraHardcore;

  private final RoleDescriptionProvider descriptionProvider;

  public RoleAttributor(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
    this.descriptionProvider = new RoleDescriptionProvider(ultraHardcore);
  }

  public RoleDescriptionProvider descriptionProvider() {
    return this.descriptionProvider;
  }

  private Constructor<? extends AbstractRole> findConstructor(
      final Class<? extends AbstractRole> roleClass, final Plugin plugin)
      throws NoSuchMethodException {
    for (final var constructor : roleClass.getConstructors()) {
      final var types = constructor.getParameterTypes();
      if (types.length == 2
          && types[0].isInstance(plugin)
          && types[1].isAssignableFrom(Profile.class)) {
        return (Constructor<? extends AbstractRole>) constructor;
      }
    }

    throw new NoSuchMethodException(
        roleClass.getName() + " has no public constructor accepting (Plugin, Profile)");
  }

  public void release(final Profile profile) {
    final RoleComponent component = profile.getComponent(RoleComponent.class);
    if (component == null) {
      profile.removeComponent(CampComponent.class);
      return;
    }

    final AbstractRole role = component.role();
    if (role instanceof final Listener listener) {
      HandlerList.unregisterAll(listener);
    }

    for (final AbstractPower power : role.getPowers()) {
      profile.unregisterPower(power.getId());
    }

    profile.removeComponent(RoleComponent.class);
    profile.removeComponent(CampComponent.class);
  }

  public void attribute(final Profile profile, final RoleMeta meta) {
    this.release(profile);

    final AbstractRole role;
    try {
      final Plugin plugin =
          this.ultraHardcore.moduleService().getCurrentModule().plugin(Plugin.class);
      role = this.findConstructor(meta.roleClass(), plugin).newInstance(plugin, profile);
    } catch (final InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    profile.addComponent(new RoleComponent(role));
    profile.addComponent(new CampComponent(meta.camp()));

    if (role instanceof final Listener listener) {
      Bukkit.getPluginManager().registerEvents(listener, this.ultraHardcore.plugin());
    }

    for (final AbstractPower power : role.getPowers()) {
      profile.registerPower(power, true);
    }

    this.applyColor(profile, meta);

    Bukkit.getScheduler()
        .runTaskLater(
            this.ultraHardcore.plugin(),
            () -> {
              profile.showTitle(
                  Title.title(
                      text()
                          .append(text('»', DARK_GRAY, BOLD))
                          .appendSpace()
                          .append(text(meta.getDefaultName(), GOLD, BOLD))
                          .appendSpace()
                          .append(text('«', DARK_GRAY, BOLD))
                          .build(),
                      text(
                          meta.camp().getName(),
                          Color.of(meta.camp().primaryColor()).asTextColor()),
                      times(ZERO, ofSeconds(3), ZERO)));

              profile.findPlayer().ifPresent(player -> role.onDistribute(profile, player));
              this.descriptionProvider.sendDescription(role);
              profile.findPlayer().ifPresent(role::onDescription);

              this.applyKnownRolesColor(profile, role);
            },
            DISTRIBUTE_DELAY_TICKS);
  }

  private void applyColor(final Profile profile, final RoleMeta meta) {
    final var holder = this.ultraHardcore.nicknameService().holder(profile.getId());
    holder.applyColor(profile.getId(), Color.of(meta.camp().primaryColor()));
  }

  private void applyKnownRolesColor(final Profile profile, final AbstractRole role) {
    final var holder = this.ultraHardcore.nicknameService().holder(profile.getId());
    for (final RoleMeta known : role.knownRoles()) {
      final var color = Color.of(known.camp().primaryColor());
      for (final Profile target : RoleUtils.findPlayersWithRole(known)) {
        holder.applyColor(target.getId(), color);
      }
    }
  }
}
