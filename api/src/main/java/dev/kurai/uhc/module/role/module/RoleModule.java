package dev.kurai.uhc.module.role.module;

import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.RoleMeta;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface RoleModule {

  Collection<RoleMeta> availableRoles();

  default Optional<? extends AbstractRole> roleOf(final UUID uniqueId) {
    return Optional.empty();
  }

  default boolean forceRole(final UUID target, final RoleMeta roleMeta) {
    return false;
  }

  boolean hiddenComposition();

  void hiddenComposition(final boolean hiddenComposition);
}
