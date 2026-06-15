package dev.kurai.uhc.module.role;

import dev.kurai.uhc.util.api.Identifiable;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRoleData<R extends AbstractRole<?>>
    implements Identifiable<Class<? extends R>> {

  private final Class<? extends R> id;

  protected AbstractRoleData(final  Class<? extends R> role) {
    this.id = role;
  }

  @Override
  public  Class<? extends R> getId() {
    return this.id;
  }
}
