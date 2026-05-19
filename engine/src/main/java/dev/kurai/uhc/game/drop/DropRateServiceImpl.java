package dev.kurai.uhc.game.drop;

import com.google.common.collect.Maps;
import dev.kurai.uhc.event.EventService;
import java.util.Collection;
import java.util.Map;
import org.bukkit.event.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class DropRateServiceImpl implements DropRateService {

  private final Map<String, AbstractDropRateModifier> modifiers;
  private final EventService eventService;

  public DropRateServiceImpl(final EventService eventService) {
    this.eventService = eventService;
    this.modifiers = Maps.newConcurrentMap();
  }

  @Override
  public Collection<AbstractDropRateModifier> getModifiers() {
    return this.modifiers.values();
  }

  @Override
  public void registerModifier(final AbstractDropRateModifier modifier) {
    if (!(modifier instanceof final Listener listener)) {
      return;
    }

    this.eventService.registerListener(listener);
    this.modifiers.put(modifier.getId(), modifier);
  }

  @Override
  public void unregisterModifier(final String id) {
    final var modifier = this.getModifier(id);
    if (modifier == null) {
      return;
    }

    this.eventService.unregisterListener((Listener) modifier);
    this.modifiers.remove(id);
  }

  @Override
  public @Nullable AbstractDropRateModifier getModifier(final String id) {
    return this.modifiers.get(id);
  }
}
