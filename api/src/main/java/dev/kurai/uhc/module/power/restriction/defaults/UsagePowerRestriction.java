package dev.kurai.uhc.module.power.restriction.defaults;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public final class UsagePowerRestriction implements PowerRestriction {

  private static final String IDENTIFIER = "cooldown";

  private int totalUsages;
  private int uses;

  public UsagePowerRestriction(final int totalUsages) {
    this.totalUsages = totalUsages;
    this.uses = 0;
  }

  @Override
  public String getId() {
    return IDENTIFIER;
  }

  @Override
  public void onUse(final AbstractPower power, final Player player) {
    this.uses++;
  }

  @Override
  public Component provideRestrictionMessage(final AbstractPower power, final Player player) {
    return prefix()
        .append(text("Vous ne pouvez plus utiliser ce pouvoir.", RED))
        .appendSpace()
        .append(text("(", DARK_GRAY))
        .append(text(this.uses, RED, BOLD))
        .append(text("/", DARK_GRAY))
        .append(text(this.totalUsages, RED))
        .append(text(")", DARK_GRAY))
        .build();
  }

  @Override
  public boolean restrictsPower(final AbstractPower power, final Player player) {
    return this.uses >= this.totalUsages;
  }

  public int getTotalUsages() {
    return this.totalUsages;
  }

  public void setTotalUsages(final int totalUsages) {
    this.totalUsages = totalUsages;
  }

  public int getUses() {
    return this.uses;
  }

  public void setUses(final int uses) {
    this.uses = uses;
  }
}
