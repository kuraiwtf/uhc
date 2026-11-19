package dev.kurai.uhc.module.role;

import java.util.Collection;
import java.util.UUID;

public interface RoleModule {

    Collection<RoleMeta> availableRoles();

    Collection<RoleMeta> enabledRoles();

    default boolean forceRole(final UUID target, final RoleMeta roleMeta) {
        return false;
    }

    boolean hiddenComposition();

    void hiddenComposition(final boolean hiddenComposition);
}
