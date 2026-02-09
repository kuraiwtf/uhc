package dev.kurai.uhc.util.api.builder;

@FunctionalInterface
public interface Buildable<T> {

  T build();
}
