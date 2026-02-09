package dev.kurai.uhc.module.role.factory;

import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.util.api.factory.Factory;

public interface RoleFactory<R extends AbstractRole<?>, D extends AbstractRoleData<? extends R>>
    extends Factory<Class<? extends R>, D> {}
