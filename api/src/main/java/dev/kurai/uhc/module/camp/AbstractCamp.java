package dev.kurai.uhc.module.camp;

import com.google.common.base.Preconditions;
import dev.kurai.uhc.module.AbstractModule;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.annotation.Identifier;
import dev.kurai.uhc.util.api.annotation.Name;
import dev.kurai.uhc.util.api.name.Nameable;

public abstract class AbstractCamp<M extends AbstractModule>
    implements Identifiable<String>, Nameable<String> {

  protected final String id;
  protected final String name;

  protected final M module;

  public AbstractCamp(final M module) {
    final var identifierAnnotation = this.getClass().getAnnotation(Identifier.class);
    Preconditions.checkNotNull(identifierAnnotation, "Identifier annotation is missing!");
    this.id = identifierAnnotation.value();

    final var nameAnnotation = this.getClass().getAnnotation(Name.class);
    Preconditions.checkNotNull(nameAnnotation, "Name annotation is missing!");
    this.name = nameAnnotation.value();

    this.module = module;
  }

  @Override
  public final String getId() {
    return this.id;
  }

  @Override
  public final String getName() {
    return this.name;
  }

  public final M getModule() {
    return this.module;
  }
}
