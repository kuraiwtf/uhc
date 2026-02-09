package dev.kurai.uhc.util.api.key;

@FunctionalInterface
public interface Keyed<T> {

  T getKey();
}
