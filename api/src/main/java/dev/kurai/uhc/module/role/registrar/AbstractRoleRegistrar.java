package dev.kurai.uhc.module.role.registrar;

import dev.kurai.uhc.module.role.AbstractRole;
import dev.kurai.uhc.module.role.AbstractRoleData;
import dev.kurai.uhc.module.role.factory.RoleFactory;
import dev.kurai.uhc.module.role.repository.RoleRepository;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRoleRegistrar<
    R extends AbstractRole<?>, D extends AbstractRoleData<R>> {

  protected final RoleFactory<@NotNull R, @NotNull D> factory;
  protected final RoleRepository<@NotNull R, @NotNull D> repository;

  public AbstractRoleRegistrar(
      final @NotNull RoleFactory<@NotNull R, @NotNull D> factory,
      final @NotNull RoleRepository<@NotNull R, @NotNull D> repository) {
    this.factory = factory;
    this.repository = repository;
  }

  public RoleFactory<@NotNull R, @NotNull D> getFactory() {
    return this.factory;
  }

  public RoleRepository<@NotNull R, @NotNull D> getRepository() {
    return this.repository;
  }

  public Optional<@NotNull D> getRoleData(final @NotNull Class<? extends R> clazz) {
    return this.repository.findById(clazz);
  }

  public void registerRole(final @NotNull Class<? extends R> clazz) {
    if (this.isRoleRegistered(clazz)) {
      return;
    }

    this.repository.save(this.factory.provideNewInstance(clazz));
  }

  public void unregisterRole(final @NotNull Class<? extends R> clazz) {
    if (!this.isRoleRegistered(clazz)) {
      return;
    }

    this.repository.deleteById(clazz);
  }

  public boolean isRoleRegistered(final @NotNull Class<? extends R> clazz) {
    return this.repository.findById(clazz).isPresent();
  }
}
