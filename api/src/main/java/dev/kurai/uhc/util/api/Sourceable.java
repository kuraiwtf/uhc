package dev.kurai.uhc.util.api;

@FunctionalInterface
public interface Sourceable<T> {

  T getSource();
}
