package dev.kurai.uhc.util.api.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import dev.kurai.uhc.util.api.Identifiable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

public interface Cache<O, T extends Identifiable<O>> extends Iterable<T> {

  @UnmodifiableView
  @NotNull
  Collection<T> findAll();

  T insert(final O id, final T entity);

  @ApiStatus.NonExtendable
  default @NotNull T insert(final T entity) {
    return this.insert(entity.getId(), entity);
  }

  void deleteById(final O id);

  @ApiStatus.NonExtendable
  default void delete(final T entity) {
    this.deleteById(entity.getId());
  }

  void deleteAll();

  @UnmodifiableView
  @NotNull
  Collection<@NotNull T> findAll(final Predicate<? super T> filter);

  @Nullable
  T findById(final O id);

  @Nullable
  T findBy(final @NotNull Predicate<? super T> filter);

  boolean existsById(final O id);

  boolean exists(final @NotNull Predicate<? super T> filter);

  @ApiStatus.NonExtendable
  default boolean exists(final T entity) {
    return this.existsById(entity.getId());
  }

  @Override
  @NotNull
  Iterator<T> iterator();
}
