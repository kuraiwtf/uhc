package dev.kurai.uhc.game.cycle.builtin;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.cycle.AbstractCycle;
import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.RoleComponent;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.CC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public final class DayCycle extends AbstractCycle {

    public DayCycle(final UltraHardcoreAPI ultraHardcore) {
        super("day", ultraHardcore);
    }

    @Override
    public void onStart() {
        this.ultraHardcore
                .gameService()
                .sendMessage(Component.text()
                        .append(Component.text(CC.BIG_SUN, NamedTextColor.YELLOW))
                        .appendSpace()
                        .append(Component.text("LE SOLEIL SE LEVE", NamedTextColor.YELLOW, TextDecoration.BOLD))
                        .appendSpace()
                        .append(Component.text(CC.BIG_SUN, NamedTextColor.YELLOW))
                        .build());

        this.ultraHardcore.worldService().getWorld().setTime(6_000L);

        for (final Profile profile : this.ultraHardcore.profileService().getPlayingProfiles()) {
            final Player player = profile.getPlayer();
            final RoleComponent roleComponent = profile.getComponent(RoleComponent.class);
            if (player != null && roleComponent != null) {
                final AbstractRole role = roleComponent.role();
                role.onDay(player);
            }
        }
    }
}
