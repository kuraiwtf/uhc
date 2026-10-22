package dev.kurai.uhc.team;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Team extends Identifiable<Integer>, Nameable<String> {

  TeamStyle style();

  default String coloredName() {
    return this.style().coloredName();
  }

  UUID leader();

  Collection<UUID> members();

  boolean contains(final UUID uniqueId);

  @Nullable Inventory inventory();
}
