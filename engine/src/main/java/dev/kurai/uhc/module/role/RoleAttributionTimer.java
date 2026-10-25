package dev.kurai.uhc.module.role;

import static dev.kurai.uhc.util.CC.prefix;
import static java.util.Collections.shuffle;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.camp.CampComponent;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.component.DeadComponent;
import dev.kurai.uhc.profile.component.SpectatorComponent;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.Color;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@Identifier(RoleAttributionTimer.IDENTIFIER)
@Name("Rôles")
@Duration(min = 15 * 60, defaultValue = 20 * 60, max = 25 * 60)
public final class RoleAttributionTimer extends AbstractTimer {

  public static final String IDENTIFIER = "roles";

  private static final int RETRY_DELAY_SECONDS = 15;
  private static final long DISTRIBUTE_DELAY_TICKS = 10L;

  private final UltraHardcoreAPI ultraHardcore;

  public RoleAttributionTimer(final UltraHardcoreAPI ultraHardcore) {
    this.ultraHardcore = ultraHardcore;
  }

  @Override
  public void onEnd() {
    if (!(this.ultraHardcore.moduleService().getCurrentModule()
        instanceof final RoleModule module)) {
      return;
    }

    final var roles = Lists.<RoleMeta>newArrayList();
    for (final var meta : module.enabledRoles()) {
      for (int i = 0; i < meta.maxAmount(); i++) {
        roles.add(meta);
      }
    }

    final var profiles =
        Lists.newArrayList(
            this.ultraHardcore
                .profileService()
                .getProfiles(
                    profile ->
                        !profile.hasComponent(SpectatorComponent.class)
                            && !profile.hasComponent(DeadComponent.class)));

    final var iterator = profiles.iterator();
    while (iterator.hasNext()) {
      final var profile = iterator.next();
      if (profile.hasComponent(RoleComponent.class)) {
        iterator.remove();
        continue;
      }

      if (profile.getPlayer() == null) {
        this.ultraHardcore.gameService().deathService().eliminate(profile, null, true);
        iterator.remove();
      }
    }

    shuffle(roles);
    shuffle(profiles);

    if (roles.size() < profiles.size()) {
      this.ultraHardcore
          .gameService()
          .sendMessage(
              prefix()
                  .append(
                      text(
                          "Impossible d'attribuer les rôles, cette dernière vient d'être décalée de "))
                  .append(text(RETRY_DELAY_SECONDS + " secondes", AQUA))
                  .append(text('.'))
                  .build());
      this.setTimeLeft(RETRY_DELAY_SECONDS);
      this.start(this.ultraHardcore.plugin());
      return;
    }

    this.ultraHardcore
        .gameService()
        .sendMessage(prefix().append(text("Attribution des rôles...")).build());

    for (final var profile : profiles) {
      this.attributeRole(profile, roles.removeFirst());
    }

    this.ultraHardcore.moduleService().getCurrentModule().onRoles();
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

  private void attributeRole(final Profile profile, final RoleMeta meta) {
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
                      Title.Times.times(
                          java.time.Duration.ZERO,
                          java.time.Duration.ofSeconds(3),
                          java.time.Duration.ZERO)));

              profile.findPlayer().ifPresent(player -> role.onDistribute(profile, player));
              role.description().forEach(profile::sendMessage);

              for (final Component message : role.description()) {
                profile.sendMessage(message);
              }

              profile.findPlayer().ifPresent(role::onDescription);
            },
            DISTRIBUTE_DELAY_TICKS);
  }

  private void applyColor(final Profile profile, final RoleMeta meta) {
    final var holder = this.ultraHardcore.nicknameService().holder(profile.getId());
    holder.applyColor(profile.getId(), Color.of(meta.camp().primaryColor()));

    // TODO: colorer aussi les rôles connus (knownRoles) de ce rôle, absent de AbstractRole.
  }
}
