package dev.kurai.uhc.command.annotation;

import java.lang.annotation.Annotation;

/** Programmatic {@link CommandMeta}, for commands whose name is only known at runtime. */
public record SimpleCommandMeta(
    String name, String[] aliases, String permission, String description, boolean async)
    implements CommandMeta {

  public static SimpleCommandMeta of(final String name) {
    return new SimpleCommandMeta(name, new String[0], "", "", false);
  }

  public static SimpleCommandMeta of(final String name, final String description) {
    return new SimpleCommandMeta(name, new String[0], "", description, false);
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return CommandMeta.class;
  }
}
