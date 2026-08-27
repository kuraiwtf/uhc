package dev.kurai.uhc.module.power;

import com.google.common.collect.Maps;
import dev.kurai.actionbar.entry.ActionbarEntry;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.event.defaults.power.PowerUseEvent;
import dev.kurai.uhc.module.power.restriction.PowerRestriction;
import dev.kurai.uhc.module.power.restriction.holder.PowerRestrictionHolder;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.Color;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public abstract class AbstractPower
    implements Identifiable<String>, Nameable<String>, PowerRestrictionHolder {

  protected final String identifier;
  protected final String name;

  protected final Map<String, PowerRestriction> restrictions;

  protected final UUID owner;
  protected final UltraHardcoreAPI ultraHardcore;

  protected final Profile profile;

  protected AbstractPower(
      final String identifier,
      final String name,
      final UUID owner,
      final UltraHardcoreAPI ultraHardcore) {
    this.identifier = identifier;
    this.name = name;
    this.owner = owner;
    this.ultraHardcore = ultraHardcore;

    this.restrictions = Maps.newConcurrentMap();

    this.profile = ultraHardcore.profileService().getOrCreateProfile(owner);
  }

  public List<String> lore() {
    return List.of("§cLa description de ce pouvoir est introuvable.");
  }

  public @Nullable ActionbarEntry provideActionbarEntry(final Player player) {
    return null;
  }

  public Color getColor() {
    return Color.GOLD;
  }

  public abstract boolean onUse(final Player player);

  public void onRemove(final Player player) {}

  public final void use(final Player player) {
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

    for (final PowerRestriction restriction : this.getRestrictions()) {
      restriction.strategy().apply(restriction, this, player);
    }
  }

  @Override
  public final Collection<PowerRestriction> getRestrictions() {
    return this.restrictions.values();
  }

  @Override
  public final void addRestriction(final PowerRestriction restriction) {
    this.restrictions.put(restriction.getId(), restriction);
  }

  @Override
  public final void removeRestriction(final String id) {
    this.restrictions.remove(id);
  }

  @Override
  public final <T extends PowerRestriction> Optional<T> findOptionalRestriction(
      final Class<T> restrictionClass, final String id) {
    return Optional.ofNullable(this.restrictions.get(id))
        .filter(restrictionClass::isInstance)
        .map(restrictionClass::cast);
  }

  public final UltraHardcoreAPI ultraHardcore() {
    return this.ultraHardcore;
  }

  public final Profile getPlayer() {
    return this.profile;
  }

  @Override
  public final String getId() {
    return this.identifier;
  }

  @Override
  public final String getName() {
    return this.name;
  }
}
