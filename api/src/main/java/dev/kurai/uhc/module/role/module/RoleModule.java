package dev.kurai.uhc.module.role.module;

import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.module.role.registrar.AbstractRoleRegistrar;

public interface RoleModule<
    C extends AbstractRole<?>,
    D extends AbstractRoleData<C>,
    R extends AbstractRoleRegistrar<C, D>> {

  R getRoleRegistrar();
}
