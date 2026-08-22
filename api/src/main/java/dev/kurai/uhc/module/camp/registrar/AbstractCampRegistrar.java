package dev.kurai.uhc.module.camp.registrar;

import dev.kurai.uhc.module.camp.AbstractCamp;
import dev.kurai.uhc.module.camp.AbstractCampData;
import dev.kurai.uhc.module.camp.factory.CampFactory;
import dev.kurai.uhc.module.camp.repository.CampRepository;
import java.util.Optional;

public abstract class AbstractCampRegistrar<
    C extends AbstractCamp<?>, D extends AbstractCampData<C>> {

  protected final CampFactory<C, D> factory;
  protected final CampRepository<C, D> repository;

  public AbstractCampRegistrar(
      final CampFactory<C, D> factory, final CampRepository<C, D> repository) {
    this.factory = factory;
    this.repository = repository;
  }

  public CampRepository<C, D> getRegistry() {
    return this.repository;
  }

  public Optional<D> getTeam(final Class<? extends C> clazz) {
    return this.repository.findById(clazz);
  }

  public void registerTeam(final Class<? extends C> clazz) {
    if (this.isRegistered(clazz)) {
      return;
    }

    this.repository.save(this.factory.provideNewInstance(clazz));
  }

  public void unregisterTeam(final Class<? extends C> clazz) {
    if (!this.isRegistered(clazz)) {
      return;
    }

    this.repository.deleteById(clazz);
  }

  public boolean isRegistered(final Class<? extends C> clazz) {
    return this.repository.findById(clazz).isPresent();
  }
}
