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

  private final Map<@NotNull O, T> cache;

  public LocalCache(final @NotNull Map<@NotNull O, T> cache) {
    this.cache = cache;
  }

  public LocalCache() {
    this(new HashMap<>(1));
  }

  @Override
  public @UnmodifiableView @NotNull Collection<@NotNull T> findAll() {
    return Collections.unmodifiableCollection(this.cache.values());
  }

  @Override
  public @NotNull T insert(final @NotNull O id, final @NotNull T entity) {
    requireNonNull(id, "id cannot be null");
    requireNonNull(entity, "entity cannot be null");

    this.cache.put(id, entity);

    return entity;
  }

  @Override
  public void deleteById(final @NotNull O id) {
    this.cache.remove(requireNonNull(id));
  }

  @Override
  public void deleteAll() {
    this.cache.clear();
  }

  @Override
  public @UnmodifiableView @NotNull Collection<@NotNull T> findAll(
      final @NotNull Predicate<? super T> filter) {
    return this.findAll().stream().filter(filter).toList();
  }

  @Override
  public @Nullable T findById(final @NotNull O id) {
    return this.cache.get(requireNonNull(id));
  }

  @Override
  public @Nullable T findBy(final @NotNull Predicate<? super T> filter) {
    return this.findAll(filter).stream().findFirst().orElse(null);
  }

  @Override
  public boolean existsById(final @NotNull O id) {
    return this.cache.containsKey(requireNonNull(id));
  }

  @Override
  public boolean exists(final @NotNull Predicate<? super T> filter) {
    return this.findBy(filter) != null;
  }

  @Override
  public @NotNull Iterator<T> iterator() {
    return this.findAll().iterator();
  }
}
