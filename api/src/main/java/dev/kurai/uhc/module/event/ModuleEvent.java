package dev.kurai.uhc.module.event;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.description.Describable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public abstract class ModuleEvent<E extends Event>
    implements Identifiable<String>, Nameable<String>, Describable<List<String>>, Listener {

  protected final String identifier;
  protected final String name;

  protected final Class<E> eventClass;

  protected int rate;
  protected boolean active;

  public ModuleEvent(
      final String identifier,
      final String name,
      final Class<E> eventClass,
      final int rate,
      final boolean active) {
    this.identifier = identifier;
    this.name = name;

    this.eventClass = eventClass;

    this.rate = rate;

    this.active = active;
  }

  public abstract ItemStack provideIcon();

  @Override
  public abstract List<String> lore();

  public abstract void onActivation(final E event);
}
