package dev.kurai.uhc.module.power.restriction.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import dev.kurai.uhc.module.power.restriction.RestrictionStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Getter
@Setter
public final class CooldownPowerRestriction implements PowerRestriction {

  private static final String IDENTIFIER = "cooldown";

  private final Plugin plugin;

  private final RestrictionStrategy strategy;

  private int initialCooldownTime;
  private int timeLeft;

  @Getter(AccessLevel.NONE)
  private BukkitTask task;

  public CooldownPowerRestriction(
      final Plugin plugin, final int initialCooldownTime, final RestrictionStrategy strategy) {
    this.plugin = plugin;
    this.strategy = strategy;
    this.initialCooldownTime = initialCooldownTime;
    this.timeLeft = 0;
  }

  public CooldownPowerRestriction(final Plugin plugin, final int initialCooldownTime) {
    this(plugin, initialCooldownTime, RestrictionStrategy.onUse());
  }

  @Override
  public String getId() {
    return IDENTIFIER;
  }

  @Override
  public void onUse(final AbstractPower power, final Player player) {
    this.timeLeft = this.initialCooldownTime;

    if (this.task != null) {
      return;
    }

    this.task = new CooldownDecrementTask(this).runTaskTimer(this.plugin, 0, 20L);
  }

  @Override
  public Component provideRestrictionMessage(final AbstractPower power, final Player player) {
    return prefix()
        .append(text("Vous devez patienter ", RED))
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

  private static final class CooldownDecrementTask extends BukkitRunnable {

    private final CooldownPowerRestriction restriction;

    private CooldownDecrementTask(final CooldownPowerRestriction restriction) {
      this.restriction = restriction;
    }

    @Override
    public void run() {
      if (this.restriction.timeLeft <= 0) {
        this.restriction.task = null;
        this.cancel();
        return;
      }

      this.restriction.timeLeft--;
    }
  }
}
