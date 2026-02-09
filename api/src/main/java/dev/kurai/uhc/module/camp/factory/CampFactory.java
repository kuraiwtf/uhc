package dev.kurai.uhc.module.camp.factory;

import dev.kurai.uhc.module.camp.AbstractCamp;
import dev.kurai.uhc.module.camp.AbstractCampData;
import dev.kurai.uhc.util.api.factory.Factory;

public interface CampFactory<T extends AbstractCamp<?>, D extends AbstractCampData<? extends T>>
    extends Factory<Class<? extends T>, D> {}
