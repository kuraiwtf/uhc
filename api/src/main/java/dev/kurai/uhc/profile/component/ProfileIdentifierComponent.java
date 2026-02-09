package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.defaults.IdentifierComponent;
import java.util.UUID;

public final class ProfileIdentifierComponent extends IdentifierComponent<UUID> {
  public ProfileIdentifierComponent() {}

  public ProfileIdentifierComponent(final UUID identifier) {
    super(identifier);
  }
}
