package dev.kurai.uhc.module.power.holder;

import dev.kurai.uhc.module.power.AbstractPower;
import java.util.Collection;

public interface PowerHolder {

  Collection<AbstractPower> getPowers();

  <T extends AbstractPower> T getPower(final Class<T> clazz);

  void registerPower(final AbstractPower power);

  default void registerPowers(final AbstractPower... powers) {
    for (final var power : powers) {
      this.registerPower(power);
    }
  }

  void unregisterPower(final String id);

  default void unregisterPowers(final String... ids) {
    for (final var id : ids) {
      this.unregisterPower(id);
    }
  }
}
