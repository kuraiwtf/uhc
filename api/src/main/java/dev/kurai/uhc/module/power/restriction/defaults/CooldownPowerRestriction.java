package dev.kurai.uhc.module.power.restriction.defaults;

import dev.kurai.uhc.event.defaults.power.cooldown.PowerCooldownDecrementEvent;
import dev.kurai.uhc.event.defaults.power.cooldown.PowerCooldownEndEvent;
import dev.kurai.uhc.event.defaults.power.cooldown.PowerCooldownStartEvent;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import dev.kurai.uhc.module.power.restriction.RestrictionStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@Getter
@Setter
public final class CooldownPowerRestriction implements PowerRestriction {

    private final String identifier;

    private final Plugin plugin;

    private final RestrictionStrategy strategy;

    private int initialCooldownTime;
    private int timeLeft;

    @Getter(AccessLevel.NONE)
    private BukkitTask task;

    public CooldownPowerRestriction(
            final String identifier,
            final Plugin plugin,
            final int initialCooldownTime,
            final RestrictionStrategy strategy) {
        this.identifier = identifier;
        this.plugin = plugin;
        this.strategy = strategy;
        this.initialCooldownTime = initialCooldownTime;
        this.timeLeft = 0;
    }

    public CooldownPowerRestriction(final String identifier, final Plugin plugin, final int initialCooldownTime) {
        this(identifier, plugin, initialCooldownTime, RestrictionStrategy.onUse());
    }

    @Override
    public String getId() {
        return this.identifier;
    }

    @Override
    public void onUse(final AbstractPower power, final Player player) {
        this.timeLeft = this.initialCooldownTime;

        Bukkit.getPluginManager().callEvent(new PowerCooldownStartEvent(player, this));

        if (this.task == null) {
            this.task = new CooldownDecrementTask(this, player.getUniqueId()).runTaskTimer(this.plugin, 0, 20L);
        }
    }

    @Override
    public Component provideRestrictionMessage(final AbstractPower power, final Player player) {
        return prefix().append(text("Vous devez patienter ", RED))
                .append(text(this.timeLeft, DARK_RED))
                .appendSpace()
                .append(text("secondes", DARK_RED))
                .append(text(" avant de pouvoir réutiliser ce pouvoir.", RED))
                .build();
    }

    @Override
    public boolean restrictsPower(final AbstractPower power, final Player player) {
        return this.timeLeft > 0;
    }

    @RequiredArgsConstructor
    private static final class CooldownDecrementTask extends BukkitRunnable {

        private final CooldownPowerRestriction restriction;
        private final UUID owner;

        @Override
        public void run() {
            final Player player = Bukkit.getPlayer(this.owner);
            if (this.restriction.timeLeft <= 0) {
                this.restriction.task = null;
                this.cancel();
                if (player != null) {
                    Bukkit.getPluginManager().callEvent(new PowerCooldownEndEvent(player, this.restriction));
                }
                return;
            }

            if (player != null) {
                final PowerCooldownDecrementEvent decrementEvent =
                        new PowerCooldownDecrementEvent(player, this.restriction);
                Bukkit.getPluginManager().callEvent(decrementEvent);
                if (decrementEvent.isCancelled()) {
                    return;
                }
            }
            this.restriction.timeLeft--;
        }
    }
}
