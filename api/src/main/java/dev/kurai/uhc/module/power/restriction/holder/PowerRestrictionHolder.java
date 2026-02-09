package dev.kurai.uhc.module.power.restriction.holder;

import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import java.util.Collection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PowerRestrictionHolder {

  Collection<@NotNull PowerRestriction> getRestrictions();

  void addRestriction(final @NotNull PowerRestriction restriction);

  default void addRestrictions(final @NotNull PowerRestriction @NotNull ... restrictions) {
    for (final var restriction : restrictions) {
      this.addRestriction(restriction);
    }
  }

  void removeRestriction(final @NotNull String id);

  default void removeRestrictions(final @NotNull String @NotNull ... ids) {
    for (final var id : ids) {
      this.removeRestriction(id);
    }
  }

  <T extends PowerRestriction> Optional<T> findOptionalRestriction(
      final @NotNull Class<T> restrictionClass, final @NotNull String id);

  default <T extends PowerRestriction> @Nullable T findRestriction(
      final @NotNull Class<T> restrictionClass, final @NotNull String id) {
    return this.findOptionalRestriction(restrictionClass, id).orElse(null);
  }
}
