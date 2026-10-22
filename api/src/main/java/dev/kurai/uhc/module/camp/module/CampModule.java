package dev.kurai.uhc.module.camp.module;

import dev.kurai.uhc.module.camp.AbstractCamp;
import dev.kurai.uhc.module.camp.AbstractCampData;
import dev.kurai.uhc.module.camp.registrar.AbstractCampRegistrar;

import java.util.Optional;
import java.util.UUID;

public interface CampModule<
    C extends AbstractCamp<?>,
    D extends AbstractCampData<C>,
    R extends AbstractCampRegistrar<C, D>> {

  R getCampRegistrar();

  default Optional<? extends AbstractCamp<?>> campOf(final UUID uniqueId) {
    return Optional.empty();
  }
}
