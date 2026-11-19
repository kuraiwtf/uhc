package dev.kurai.uhc.module.camp;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CampModule {

    Collection<Camp> availableCamps();

    default Optional<? extends Camp> campOf(final UUID uniqueId) {
        return Optional.empty();
    }
}
