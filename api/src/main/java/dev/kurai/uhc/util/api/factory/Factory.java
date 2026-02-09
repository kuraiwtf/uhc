package dev.kurai.uhc.util.api.factory;

import dev.kurai.uhc.util.api.Identifiable;

@FunctionalInterface
public interface Factory<I, O extends Identifiable<I>> {

  O provideNewInstance(final I identifier);
}
