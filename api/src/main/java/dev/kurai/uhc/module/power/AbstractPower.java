package dev.kurai.uhc.module.power;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.actionbar.ActionbarEntry;
import dev.kurai.uhc.event.defaults.power.PowerUseEvent;
import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import dev.kurai.uhc.module.power.restriction.holder.PowerRestrictionHolder;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPower
    implements Identifiable<@NotNull String>, Nameable<@NotNull String>, PowerRestrictionHolder {

  protected final String identifier;
  protected final String name;

  protected final Map<@NotNull String, @NotNull PowerRestriction> restrictions;

  protected final UUID owner;
  protected final UltraHardcoreAPI ultraHardcore;

  protected final Profile profile;

  public AbstractPower(
      final @NotNull String identifier,
      final @NotNull String name,
      final @NotNull UUID owner,
      final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.identifier = identifier;
    this.name = name;
    this.owner = owner;
    this.ultraHardcore = ultraHardcore;

    this.restrictions = Maps.newConcurrentMap();

    this.profile = ultraHardcore.profileService().getOrCreateProfile(owner);
  }

  public @Nullable ActionbarEntry provideActionbarEntry(final @NotNull Player player) {
    return null;
  }

  public abstract boolean onUse(final @NotNull Player player);

  public void onRemove(final @NotNull Player player) {}

  public final void use(final @NotNull Player player) {
    for (final var restriction : this.restrictions.values()) {
      if (restriction.restrictsPower(this, player)) {
        this.profile.sendMessage(restriction.provideRestrictionMessage(this, player));
        return;
      }
    }

    final var powerUseEvent =
        this.ultraHardcore.eventService().dispatchEvent(new PowerUseEvent(this.profile, this));
    if (powerUseEvent.isCancelled()) {
      return;
    }

    if (!this.onUse(player)) {
      return;
    }

    this.restrictions.values().forEach(restriction -> restriction.onUse(this, player));
  }

  @Override
  public final Collection<@NotNull PowerRestriction> getRestrictions() {
    return this.restrictions.values();
  }

  @Override
  public final void addRestriction(final @NotNull PowerRestriction restriction) {
    this.restrictions.put(restriction.getId(), restriction);
  }

  @Override
  public final void removeRestriction(final @NotNull String id) {
    this.restrictions.remove(id);
  }

  @Override
  public final <T extends PowerRestriction> Optional<T> findOptionalRestriction(
      final @NotNull Class<T> restrictionClass, final @NotNull String id) {
    return Optional.ofNullable(this.restrictions.get(id))
        .filter(restrictionClass::isInstance)
        .map(restrictionClass::cast);
  }

  public final UltraHardcoreAPI ultraHardcore() {
    return this.ultraHardcore;
  }

  public final @NotNull Profile getPlayer() {
    return this.profile;
  }

  @Override
  public final @NotNull String getId() {
    return this.identifier;
  }

  @Override
  public final @NotNull String getName() {
    return this.name;
  }
}
