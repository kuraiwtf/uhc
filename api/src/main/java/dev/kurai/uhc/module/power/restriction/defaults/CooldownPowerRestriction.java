package dev.kurai.uhc.module.power.restriction.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class CooldownPowerRestriction implements PowerRestriction {

  private static final String IDENTIFIER = "cooldown";

  private final Plugin plugin;

  private int initialCooldownTime;
  private int timeLeft;

  private BukkitTask task;

  public CooldownPowerRestriction(final @NotNull Plugin plugin, final int initialCooldownTime) {
    this.plugin = plugin;
    this.initialCooldownTime = initialCooldownTime;
    this.timeLeft = 0;
  }

  @Override
  public @NotNull String getId() {
    return IDENTIFIER;
  }

  @Override
  public void onUse(final @NotNull AbstractPower power, final @NotNull Player player) {
    this.timeLeft = this.initialCooldownTime;

    if (this.task != null) {
      return;
    }

    this.task = new CooldownDecrementTask(this).runTaskTimer(this.plugin, 0, 20L);
  }

  @Override
  public @NotNull Component provideRestrictionMessage(
      final @NotNull AbstractPower power, final @NotNull Player player) {
    return prefix()
        .append(text("Vous devez patienter ", RED))
        .append(text(this.timeLeft, DARK_RED))
        .appendSpace()
        .append(text("secondes", DARK_RED))
        .append(text(" avant de pouvoir réutiliser ce pouvoir.", RED))
        .build();
  }

  @Override
  public boolean restrictsPower(final @NotNull AbstractPower power, final @NotNull Player player) {
    return this.timeLeft > 0;
  }

  public int getInitialCooldownTime() {
    return this.initialCooldownTime;
  }

  public void setInitialCooldownTime(final int initialCooldownTime) {
    this.initialCooldownTime = initialCooldownTime;
  }

  public int getTimeLeft() {
    return this.timeLeft;
  }

  public void setTimeLeft(final int timeLeft) {
    this.timeLeft = timeLeft;
  }

  private static final class CooldownDecrementTask extends BukkitRunnable {

    private final CooldownPowerRestriction restriction;

    private CooldownDecrementTask(final @NotNull CooldownPowerRestriction restriction) {
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
