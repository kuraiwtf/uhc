package dev.kurai.uhc.module.camp.registrar;

import dev.kurai.uhc.module.camp.AbstractCamp;
import dev.kurai.uhc.module.camp.AbstractCampData;
import dev.kurai.uhc.module.camp.factory.CampFactory;
import dev.kurai.uhc.module.camp.repository.CampRepository;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCampRegistrar<
    C extends AbstractCamp<?>, D extends AbstractCampData<C>> {

  protected final CampFactory<@NotNull C, @NotNull D> factory;
  protected final CampRepository<@NotNull C, @NotNull D> repository;

  public AbstractCampRegistrar(
      final CampFactory<@NotNull C, @NotNull D> factory,
      final CampRepository<@NotNull C, @NotNull D> repository) {
    this.factory = factory;
    this.repository = repository;
  }

  public CampRepository<@NotNull C, @NotNull D> getRegistry() {
    return this.repository;
  }

  public Optional<@NotNull D> getTeam(final @NotNull Class<? extends C> clazz) {
    return this.repository.findById(clazz);
  }

  public void registerTeam(final @NotNull Class<? extends C> clazz) {
    if (this.isRegistered(clazz)) {
      return;
    }

    this.repository.save(this.factory.provideNewInstance(clazz));
  }

  public void unregisterTeam(final @NotNull Class<? extends C> clazz) {
    if (!this.isRegistered(clazz)) {
      return;
    }

    this.repository.deleteById(clazz);
  }

  public boolean isRegistered(final @NotNull Class<? extends C> clazz) {
    return this.repository.findById(clazz).isPresent();
  }
}
