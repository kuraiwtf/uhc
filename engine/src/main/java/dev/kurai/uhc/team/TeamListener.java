package dev.kurai.uhc.team;

import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.module.team.module.TeamModule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TeamListener implements Listener {

    private final TeamService teamService;
    private final ModuleService moduleService;

    public TeamListener(final TeamService teamService, final ModuleService moduleService) {
        this.teamService = teamService;
        this.moduleService = moduleService;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFriendlyFire(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof final Player victim)
                || !(this.moduleService.getCurrentModule() instanceof final TeamModule module)
                || module.friendlyFire()) {
            return;
        }

        final Player attacker = event.getDamager() instanceof final Player player
                ? player
                : event.getDamager() instanceof final Projectile projectile
                                && projectile.getShooter() instanceof final Player shooter
                        ? shooter
                        : null;
        if (attacker != null && this.teamService.areTeammates(attacker.getUniqueId(), victim.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
