package dev.kurai.uhc.module.power.restriction;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@FunctionalInterface
@NullMarked
public interface RestrictionStrategy {

  @Contract(pure = true)
  static RestrictionStrategy onUse() {
    return PowerRestriction::onUse;
  }

  static RestrictionStrategy delayed(final int delayTicks) {
    return (restriction, power, player) ->
        Bukkit.getScheduler()
            .runTaskLater(
                UltraHardcoreAPI.getInstance().plugin(),
                () -> restriction.onUse(power, player),
                delayTicks);
  }

  void apply(final PowerRestriction restriction, final AbstractPower power, final Player player);
}
