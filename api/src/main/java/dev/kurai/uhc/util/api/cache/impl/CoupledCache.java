package dev.kurai.uhc.util.api.cache.impl;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.cache.Cache;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.Nullable;

public class CoupledCache<O, T extends Identifiable<O>> implements Cache<O, T> {

  private final Cache<O, T> nearCache, sourceOfTruthCache;

  public CoupledCache(final Cache<O, T> nearCache, final Cache<O, T> sourceOfTruthCache) {
    this.nearCache = nearCache;
    this.sourceOfTruthCache = sourceOfTruthCache;
  }

  public CoupledCache(final Cache<O, T> sourceOfTruthCache /* e. g RedisCache<O, T> */) {
    this(new LocalCache<>(), sourceOfTruthCache);
  }

  @Override
  public @UnmodifiableView Collection<T> findAll() {
    final var nearCacheEntities = this.nearCache.findAll();

    return nearCacheEntities.isEmpty() ? this.sourceOfTruthCache.findAll() : nearCacheEntities;
  }

  @Override
  public T insert(final O id, final T entity) {
    return this.nearCache.insert(id, entity);
  }

  public T insertRemote(final O id, final T entity) {
    return this.sourceOfTruthCache.insert(id, entity);
  }

  public T insertRemote(final T entity) {
    return this.sourceOfTruthCache.insert(entity);
  }

  @Override
  public void deleteById(final O id) {
    this.nearCache.deleteById(id);
  }

  @Override
  public void deleteAll() {
    this.nearCache.deleteAll();
  }

  public void deleteRemote(final T entity) {
    this.sourceOfTruthCache.deleteById(entity.getId());
  }

  public void deleteByIdRemote(final O id) {
    this.sourceOfTruthCache.deleteById(id);
  }

  public void deleteAllRemote() {
    this.sourceOfTruthCache.deleteAll();
  }

  @Override
  public @UnmodifiableView Collection<T> findAll(final Predicate<? super T> filter) {
    final var nearCacheEntities = this.nearCache.findAll(filter);

    return nearCacheEntities.isEmpty()
        ? this.sourceOfTruthCache.findAll(filter)
        : nearCacheEntities;
  }

  @Override
  public @Nullable T findById(final O id) {
    final var nearCacheEntity = this.nearCache.findById(id);

    return nearCacheEntity == null ? this.sourceOfTruthCache.findById(id) : nearCacheEntity;
  }

  @Override
  public @Nullable T findBy(final Predicate<? super T> filter) {
    final var nearCacheEntity = this.nearCache.findBy(filter);

    return nearCacheEntity == null ? this.sourceOfTruthCache.findBy(filter) : nearCacheEntity;
  }

  @Override
  public boolean existsById(final O id) {
    return this.nearCache.existsById(id) || this.sourceOfTruthCache.existsById(id);
  }

  @Override
  public boolean exists(final Predicate<? super T> filter) {
    return this.nearCache.exists(filter) || this.sourceOfTruthCache.exists(filter);
  }

  @Override
  public Iterator<T> iterator() {
    return this.nearCache.iterator();
  }

  public Iterator<T> iteratorRemote() {
    return this.sourceOfTruthCache.iterator();
  }
}
