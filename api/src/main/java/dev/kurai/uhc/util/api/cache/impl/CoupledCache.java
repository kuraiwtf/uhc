package dev.kurai.uhc.util.api.cache.impl;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.cache.Cache;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

public class CoupledCache<O, T extends Identifiable<O>> implements Cache<O, T> {

  private final Cache<O, T> nearCache, sourceOfTruthCache;

  public CoupledCache(
      final @NotNull Cache<O, T> nearCache, final @NotNull Cache<O, T> sourceOfTruthCache) {
    this.nearCache = nearCache;
    this.sourceOfTruthCache = sourceOfTruthCache;
  }

  public CoupledCache(final @NotNull Cache<O, T> sourceOfTruthCache /* e. g RedisCache<O, T> */) {
    this(new LocalCache<>(), sourceOfTruthCache);
  }

  @Override
  public @UnmodifiableView @NotNull Collection<@NotNull T> findAll() {
    final var nearCacheEntities = this.nearCache.findAll();

    return nearCacheEntities.isEmpty() ? this.sourceOfTruthCache.findAll() : nearCacheEntities;
  }

  @Override
  public @NotNull T insert(final @NotNull O id, final @NotNull T entity) {
    return this.nearCache.insert(id, entity);
  }

  public @NotNull T insertRemote(@NotNull final O id, final @NotNull T entity) {
    return this.sourceOfTruthCache.insert(id, entity);
  }

  public @NotNull T insertRemote(final @NotNull T entity) {
    return this.sourceOfTruthCache.insert(entity);
  }

  @Override
  public void deleteById(final @NotNull O id) {
    this.nearCache.deleteById(id);
  }

  @Override
  public void deleteAll() {
    this.nearCache.deleteAll();
  }

  public void deleteRemote(final @NotNull T entity) {
    this.sourceOfTruthCache.deleteById(entity.getId());
  }

  public void deleteByIdRemote(final @NotNull O id) {
    this.sourceOfTruthCache.deleteById(id);
  }

  public void deleteAllRemote() {
    this.sourceOfTruthCache.deleteAll();
  }

  @Override
  public @UnmodifiableView @NotNull Collection<@NotNull T> findAll(
      final @NotNull Predicate<? super T> filter) {
    final var nearCacheEntities = this.nearCache.findAll(filter);

    return nearCacheEntities.isEmpty()
        ? this.sourceOfTruthCache.findAll(filter)
        : nearCacheEntities;
  }

  @Override
  public @Nullable T findById(final @NotNull O id) {
    final var nearCacheEntity = this.nearCache.findById(id);

    return nearCacheEntity == null ? this.sourceOfTruthCache.findById(id) : nearCacheEntity;
  }

  @Override
  public @Nullable T findBy(final @NotNull Predicate<? super T> filter) {
    final var nearCacheEntity = this.nearCache.findBy(filter);

    return nearCacheEntity == null ? this.sourceOfTruthCache.findBy(filter) : nearCacheEntity;
  }

  @Override
  public boolean existsById(final @NotNull O id) {
    return this.nearCache.existsById(id) || this.sourceOfTruthCache.existsById(id);
  }

  @Override
  public boolean exists(final @NotNull Predicate<? super T> filter) {
    return this.nearCache.exists(filter) || this.sourceOfTruthCache.exists(filter);
  }

  @Override
  public @NotNull Iterator<T> iterator() {
    return this.nearCache.iterator();
  }

  public @NotNull Iterator<T> iteratorRemote() {
    return this.sourceOfTruthCache.iterator();
  }
}
