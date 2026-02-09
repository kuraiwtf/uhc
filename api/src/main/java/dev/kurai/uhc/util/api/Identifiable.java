package dev.kurai.uhc.util.api;

@FunctionalInterface
public interface Identifiable<T> {

  T getId();
}
