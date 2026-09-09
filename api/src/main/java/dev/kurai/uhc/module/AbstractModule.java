package dev.kurai.uhc.module;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.ecs.component.defaults.IdentifierComponent;
import dev.kurai.uhc.ecs.component.defaults.NameComponent;
import dev.kurai.uhc.ecs.entity.Entity;
import dev.kurai.uhc.menu.module.ModuleMenu;
import dev.kurai.uhc.module.component.ModuleShortNameComponent;
import dev.kurai.uhc.module.event.ModuleEvent;
import dev.kurai.uhc.module.event.ModuleEventHolder;
import dev.kurai.uhc.profile.component.*;
import dev.kurai.uhc.util.Color;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import java.util.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class AbstractModule
    implements Entity<String>, Identifiable<String>, Nameable<String>, ModuleEventHolder {

  protected final Map<Class<? extends Component>, Component> components;

  protected final @Nullable String commandName;

  protected final UltraHardcoreAPI ultraHardcore;

  protected final Color color;

  protected final Map<String, ModuleEvent<?>> events;
  protected final Collection<ModuleEvent<?>> eventsView;

  protected AbstractModule(
      final String id,
      final String name,
      final @Nullable String commandName,
      final UltraHardcoreAPI ultraHardcore) {
    this(id, name, commandName, ultraHardcore, Color.GOLD);
  }

  protected AbstractModule(
      final String id,
      final String name,
      final @Nullable String commandName,
      final UltraHardcoreAPI ultraHardcore,
      final Color color) {
    this.color = color;
    this.components = Maps.newHashMap();
    this.addComponents(new IdentifierComponent<>(id), new NameComponent(name));

    this.commandName = commandName;

    this.ultraHardcore = ultraHardcore;

    this.events = Maps.newHashMap();
    this.eventsView = Collections.unmodifiableCollection(this.events.values());
  }

  public Color color() {
    return this.color;
  }

  public abstract String developer();

  public net.kyori.adventure.text.Component sidebarCredit() {
    return MiniMessage.miniMessage().deserialize("<dark_aqua>@</dark_aqua><aqua>kuraiwtf</aqua>");
  }

  @Override
  public Collection<Component> getComponents() {
    return this.components.values();
  }

  @Override
  public <E extends Entity<String>> E addComponent(final Component component) {
    this.components.put(component.getClass(), component);
    return (E) this;
  }

  @Override
  public boolean removeComponent(final Class<? extends Component> componentClass) {
    return this.components.remove(componentClass) != null;
  }

  @Override
  public boolean hasComponent(final Class<? extends Component> componentClass) {
    return this.components.containsKey(componentClass);
  }

  @Override
  public <T extends Component> @Nullable T getComponent(final Class<T> componentClass) {
    return Optional.ofNullable(this.components.get(componentClass))
        .map(componentClass::cast)
        .orElse(null);
  }

  @Override
  public final String getId() {
    final var component = this.getComponent(IdentifierComponent.class);
    if (component == null) {
      return "";
    }

    return component.getIdentifier().toString();
  }

  @Override
  public final String getName() {
    final var component = this.getComponent(NameComponent.class);
    if (component == null) {
      return "";
    }

    return component.getName();
  }

  public final String getShortName() {
    final var component = this.getComponent(ModuleShortNameComponent.class);
    if (component == null) {
      return this.getName();
    }

    return component.shortName();
  }

  public final @Nullable String getCommandName() {
    return this.commandName;
  }

  public final UltraHardcoreAPI getUltraHardcore() {
    return this.ultraHardcore;
  }

  @Override
  public Collection<ModuleEvent<?>> events() {
    return this.eventsView;
  }

  @Override
  public void registerEvent(final ModuleEvent<?> event) {
    this.events.put(event.identifier(), event);
  }

  @Override
  public void unregisterEvent(final String identifier) {
    this.events.remove(identifier);
  }

  @Override
  public @Nullable <E extends Event> ModuleEvent<E> event(final String identifier) {
    return (ModuleEvent<E>) this.events.get(identifier);
  }

  public @Nullable ModuleMenu provideModuleMenu(final Player player) {
    return null;
  }

  public abstract ItemStack provideModuleIcon(final Player player);
}
