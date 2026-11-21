package dev.kurai.uhc.module.role;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.GameService;
import dev.kurai.uhc.profile.component.DeadComponent;
import dev.kurai.uhc.profile.component.SpectatingProfileComponent;
import dev.kurai.uhc.timer.AbstractTimer;
import dev.kurai.uhc.timer.annotation.Duration;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;

import static dev.kurai.uhc.util.CC.prefix;
import static java.util.Collections.shuffle;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;

@Identifier(RoleAttributionTimer.IDENTIFIER)
@Name("Rôles")
@Duration(min = 15 * 60, defaultValue = 20 * 60, max = 25 * 60)
public final class RoleAttributionTimer extends AbstractTimer {

    public static final String IDENTIFIER = "roles";

    private static final int RETRY_DELAY_SECONDS = 15;

    private final UltraHardcoreAPI ultraHardcore;
    private final RoleAttributor attributor;

    public RoleAttributionTimer(final UltraHardcoreAPI ultraHardcore) {
        this.ultraHardcore = ultraHardcore;
        this.attributor = new RoleAttributor(ultraHardcore);
    }

    @Override
    public void onEnd() {
        if (!(this.ultraHardcore.moduleService().getCurrentModule() instanceof final RoleModule module)) {
            return;
        }

        final var roles = Lists.<RoleMeta>newArrayList();
        for (final var meta : module.enabledRoles()) {
            for (int i = 0; i < meta.maxAmount(); i++) {
                roles.add(meta);
            }
        }

        final var profiles = Lists.newArrayList(this.ultraHardcore
                .profileService()
                .getProfiles(profile ->
                        !profile.hasComponent(SpectatingProfileComponent.class) && !profile.hasComponent(DeadComponent.class)));

        final var iterator = profiles.iterator();
        final GameService gameService = this.ultraHardcore.gameService();
        while (iterator.hasNext()) {
            final var profile = iterator.next();
            if (profile.hasComponent(RoleComponent.class)) {
                iterator.remove();
                continue;
            }

            if (profile.getPlayer() == null) {
                gameService.deathService().eliminate(profile, null, true);
                iterator.remove();
            }
        }

        shuffle(roles);
        shuffle(profiles);

        if (roles.size() < profiles.size()) {
            gameService.sendMessage(
                    prefix().append(text("Impossible d'attribuer les rôles, cette dernière vient d'être décalée de "))
                            .append(text(RETRY_DELAY_SECONDS + " secondes", AQUA))
                            .append(text('.'))
                            .build());
            this.setTimeLeft(RETRY_DELAY_SECONDS);
            this.start(this.ultraHardcore.plugin());
            return;
        }

        gameService.sendMessage(
                prefix().append(text("Attribution des rôles...")).build());

        for (final var profile : profiles) {
            final var meta = roles.removeFirst();
            try {
                this.attributor.attribute(profile, meta);
            } catch (final RuntimeException e) {
                this.ultraHardcore
                        .plugin()
                        .getLogger()
                        .log(
                                java.util.logging.Level.SEVERE,
                                "Could not attribute role " + meta.getName() + " to " + profile.getName(),
                                e);
            }
        }

        module.onRoles();
    }
}
