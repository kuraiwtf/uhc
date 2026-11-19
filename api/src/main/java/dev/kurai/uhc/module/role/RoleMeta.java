package dev.kurai.uhc.module.role;

import dev.kurai.uhc.module.camp.Camp;

public interface RoleMeta {

    String name();

    String getDefaultName();

    String getName();

    Camp camp();

    Class<? extends AbstractRole> roleClass();

    default int maxAmount() {
        return 1;
    }
}
