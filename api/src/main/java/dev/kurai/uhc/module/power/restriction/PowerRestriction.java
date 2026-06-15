package dev.kurai.uhc.module.power.restriction;

import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.util.api.Identifiable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface PowerRestriction extends Identifiable<String> {

  @Override
  String getId();

  void onUse(final AbstractPower power, final Player player);

  Component provideRestrictionMessage(final AbstractPower power, final Player player);

  boolean restrictsPower(final AbstractPower power, final Player player);
}
