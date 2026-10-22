package dev.kurai.uhc.module.role.module;

import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.module.role.registrar.AbstractRoleRegistrar;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface RoleModule<
    C extends AbstractRole<?>,
    D extends AbstractRoleData<C>,
    R extends AbstractRoleRegistrar<C, D>> {

  R getRoleRegistrar();

  default Collection<D> getEnabledRoles() {
    return this.getRoleRegistrar().getRepository().findAll();
  }

  default Optional<? extends AbstractRole<?>> roleOf(final UUID uniqueId) {
    return Optional.empty();
  }

  default boolean forceRole(final UUID target, final AbstractRoleData<?> roleData) {
    return false;
  }

  boolean hiddenComposition();

  void hiddenComposition(final boolean hiddenComposition);
}
