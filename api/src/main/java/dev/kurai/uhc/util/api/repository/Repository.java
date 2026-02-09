package dev.kurai.uhc.util.api.repository;

import dev.kurai.uhc.util.api.Identifiable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class Repository<I, E extends Identifiable<I>> implements Iterable<E> {

  private final Map<I, E> repository;

  public Repository(final Map<I, E> repository) {
    this.repository = repository;
  }

  public Repository() {
    this(new HashMap<>());
  }

  public Map<I, E> repository() {
    return this.repository;
  }

  public E insert(final E entity) {
    this.repository.put(entity.getId(), entity);
    return entity;
  }

  public E save(final E entity) {
    return this.repository.put(entity.getId(), entity);
  }

  public E delete(final E entity) {
    return this.deleteById(entity.getId());
  }

  public E deleteById(final I id) {
    return this.repository.remove(id);
  }

  public Optional<E> findById(final I id) {
    return Optional.ofNullable(this.repository.get(id));
  }

  public Optional<E> findBy(final Predicate<E> filter) {
    return this.stream().filter(filter).findFirst();
  }

  public Collection<E> findAll() {
    return this.repository.values();
  }

  public Collection<E> findAllBy(final Predicate<E> filter) {
    return this.stream().filter(filter).toList();
  }

  public Stream<E> newStream(final Collection<E> entities) {
    return entities.stream();
  }

  public Stream<E> stream() {
    return this.newStream(this.findAll());
  }

  public int size() {
    return this.repository.size();
  }

  @Override
  public @NotNull Iterator<E> iterator() {
    return this.findAll().iterator();
  }

  public boolean existsById(final I id) {
    return this.repository.containsKey(id);
  }

  public boolean exists(final Predicate<E> filter) {
    return this.stream().anyMatch(filter);
  }
}
