package dev.kurai.uhc.module.power.restriction;

import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.util.api.Identifiable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PowerRestriction extends Identifiable<@NotNull String> {

  @Override
  @NotNull
  String getId();

  void onUse(final @NotNull AbstractPower power, final @NotNull Player player);

  @NotNull
  Component provideRestrictionMessage(
      final @NotNull AbstractPower power, final @NotNull Player player);

  boolean restrictsPower(final @NotNull AbstractPower power, final @NotNull Player player);
}
