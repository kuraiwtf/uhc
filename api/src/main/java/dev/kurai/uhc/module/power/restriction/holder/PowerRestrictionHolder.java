package dev.kurai.uhc.module.power.restriction.holder;

import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public interface PowerRestrictionHolder {

  Collection<PowerRestriction> getRestrictions();

  void addRestriction(final PowerRestriction restriction);

  default void addRestrictions(final PowerRestriction... restrictions) {
    for (final var restriction : restrictions) {
      this.addRestriction(restriction);
    }
  }

  void removeRestriction(final String id);

  default void removeRestrictions(final String... ids) {
    for (final var id : ids) {
      this.removeRestriction(id);
    }
  }

  <T extends PowerRestriction> Optional<T> findOptionalRestriction(
      final Class<T> restrictionClass, final String id);

  default <T extends PowerRestriction> @Nullable T findRestriction(
      final Class<T> restrictionClass, final String id) {
    return this.findOptionalRestriction(restrictionClass, id).orElse(null);
  }
}
