package dev.kurai.uhc.module.role.repository;

import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.util.api.repository.Repository;

public final class RoleRepository<R extends AbstractRole<?>, D extends AbstractRoleData<R>>
    extends Repository<Class<? extends R>, D> {}
