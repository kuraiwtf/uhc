package dev.kurai.uhc.util.api.repository.impl;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.repository.Repository;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentRepository<I, E extends Identifiable<I>> extends Repository<I, E> {

  public ConcurrentRepository() {
    super(new ConcurrentHashMap<>());
  }
}
