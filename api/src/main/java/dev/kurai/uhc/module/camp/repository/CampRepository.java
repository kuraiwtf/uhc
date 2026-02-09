package dev.kurai.uhc.module.camp.repository;

import dev.kurai.uhc.module.camp.AbstractCamp;
import dev.kurai.uhc.module.camp.AbstractCampData;
import dev.kurai.uhc.util.api.repository.Repository;

public final class CampRepository<T extends AbstractCamp<?>, D extends AbstractCampData<T>>
    extends Repository<Class<? extends T>, D> {}
