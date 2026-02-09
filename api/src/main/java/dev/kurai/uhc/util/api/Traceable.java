package dev.kurai.uhc.util.api;

@FunctionalInterface
public interface Traceable<T> {

  T getCreatedAt();
}
