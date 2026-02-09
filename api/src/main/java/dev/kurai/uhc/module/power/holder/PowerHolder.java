package dev.kurai.uhc.module.power.holder;

import dev.kurai.uhc.module.power.AbstractPower;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public interface PowerHolder {

  @NotNull
  Collection<@NotNull AbstractPower> getPowers();

  void registerPower(final @NotNull AbstractPower power);

  default void registerPowers(final AbstractPower @NotNull ... powers) {
    for (final var power : powers) {
      this.registerPower(power);
    }
  }

  void unregisterPower(final @NotNull String id);

  default void unregisterPowers(final String @NotNull ... ids) {
    for (final var id : ids) {
      this.unregisterPower(id);
    }
  }
}
