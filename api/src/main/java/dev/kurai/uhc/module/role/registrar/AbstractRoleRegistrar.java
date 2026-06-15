package dev.kurai.uhc.module.role.registrar;

import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.module.role.factory.RoleFactory;
import dev.kurai.uhc.module.role.repository.RoleRepository;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRoleRegistrar<
    R extends AbstractRole<?>, D extends AbstractRoleData<R>> {

  protected final RoleFactory< R,  D> factory;
  protected final RoleRepository< R,  D> repository;

  public AbstractRoleRegistrar(
      final  RoleFactory< R,  D> factory,
      final  RoleRepository< R,  D> repository) {
    this.factory = factory;
    this.repository = repository;
  }

  public RoleFactory< R,  D> getFactory() {
    return this.factory;
  }

  public RoleRepository< R,  D> getRepository() {
    return this.repository;
  }

  public Optional< D> getRoleData(final  Class<? extends R> clazz) {
    return this.repository.findById(clazz);
  }

  public void registerRole(final  Class<? extends R> clazz) {
    if (this.isRoleRegistered(clazz)) {
      return;
    }

    this.repository.save(this.factory.provideNewInstance(clazz));
  }

  public void unregisterRole(final  Class<? extends R> clazz) {
    if (!this.isRoleRegistered(clazz)) {
      return;
    }

    this.repository.deleteById(clazz);
  }

  public boolean isRoleRegistered(final  Class<? extends R> clazz) {
    return this.repository.findById(clazz).isPresent();
  }
}
