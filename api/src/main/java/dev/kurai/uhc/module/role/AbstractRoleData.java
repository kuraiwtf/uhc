package dev.kurai.uhc.module.role;

import dev.kurai.uhc.module.camp.AbstractCamp;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.annotation.Name;
import org.jspecify.annotations.Nullable;

public abstract class AbstractRoleData<R extends AbstractRole<?>>
    implements Identifiable<Class<? extends R>> {

  private final Class<? extends R> id;

  protected AbstractRoleData(final Class<? extends R> role) {
    this.id = role;
  }

  @Override
  public Class<? extends R> getId() {
    return this.id;
  }

  /** Display name of the role, read from {@link Name} by default. */
  public String getName() {
    final var name = this.id.getAnnotation(Name.class);
    return name == null ? this.id.getSimpleName() : name.value();
  }

  /** The camp of this role, or {@code null} if the module has no camps. */
  public @Nullable Class<? extends AbstractCamp<?>> getCampClass() {
    return null;
  }
}
