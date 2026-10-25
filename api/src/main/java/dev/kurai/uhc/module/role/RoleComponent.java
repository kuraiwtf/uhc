package dev.kurai.uhc.module.role;

import dev.kurai.uhc.ecs.component.Component;

public record RoleComponent(AbstractRole role) implements Component {}
