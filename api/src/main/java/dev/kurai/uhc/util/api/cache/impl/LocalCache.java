package dev.kurai.uhc.util.api.cache.impl;

import static java.util.Objects.requireNonNull;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.cache.Cache;
import java.util.*;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

public class LocalCache<O, T extends Identifiable<O>> implements Cache<O, T> {

  private final Map< O, T> cache;

  public LocalCache(final  Map< O, T> cache) {
    this.cache = cache;
  }

  public LocalCache() {
    this(new HashMap<>(1));
  }

  @Override
  public @UnmodifiableView  Collection< T> findAll() {
    return Collections.unmodifiableCollection(this.cache.values());
  }

  @Override
  public  T insert(final  O id, final  T entity) {
    requireNonNull(id, "id cannot be null");
    requireNonNull(entity, "entity cannot be null");

    this.cache.put(id, entity);

    return entity;
  }

  @Override
  public void deleteById(final  O id) {
    this.cache.remove(requireNonNull(id));
  }

  @Override
  public void deleteAll() {
    this.cache.clear();
  }

  @Override
  public @UnmodifiableView  Collection< T> findAll(
      final  Predicate<? super T> filter) {
    return this.findAll().stream().filter(filter).toList();
  }

  @Override
  public @Nullable T findById(final  O id) {
    return this.cache.get(requireNonNull(id));
  }

  @Override
  public @Nullable T findBy(final  Predicate<? super T> filter) {
    return this.findAll(filter).stream().findFirst().orElse(null);
  }

  @Override
  public boolean existsById(final  O id) {
    return this.cache.containsKey(requireNonNull(id));
  }

  @Override
  public boolean exists(final  Predicate<? super T> filter) {
    return this.findBy(filter) != null;
  }

  @Override
  public  Iterator<T> iterator() {
    return this.findAll().iterator();
  }
}
