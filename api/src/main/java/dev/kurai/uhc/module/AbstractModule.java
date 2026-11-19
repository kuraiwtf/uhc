package dev.kurai.uhc.module;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.ecs.component.defaults.IdentifierComponent;
import dev.kurai.uhc.ecs.component.defaults.NameComponent;
import dev.kurai.uhc.ecs.entity.Entity;
import dev.kurai.uhc.ecs.entity.EntityBridge;
import dev.kurai.uhc.menu.module.ModuleMenu;
import dev.kurai.uhc.module.component.ModuleShortNameComponent;
import dev.kurai.uhc.module.event.ModuleEvent;
import dev.kurai.uhc.module.event.ModuleEventHolder;
import dev.kurai.uhc.util.Color;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@NullMarked
public abstract class AbstractModule
        implements EntityBridge<String>, Identifiable<String>, Nameable<String>, ModuleEventHolder {

    protected final Entity<String> entity;

    protected final Plugin plugin;
    protected final UltraHardcoreAPI ultraHardcore;

    protected final @Nullable String commandName;

    protected final Map<String, ModuleEvent> events;
    protected final Collection<ModuleEvent> eventsView;

    protected AbstractModule(
            final String id,
            final String name,
            final Plugin plugin,
            final @Nullable String commandName,
            final UltraHardcoreAPI ultraHardcore) {
        this.entity = new Entity<>(id);
        this.addComponent(new NameComponent(name));

        this.plugin = plugin;
        this.ultraHardcore = ultraHardcore;

        this.commandName = commandName;

        this.events = Maps.newHashMap();
        this.eventsView = Collections.unmodifiableCollection(this.events.values());
    }

    @Override
    public Entity<String> asEntity() {
        return this.entity;
    }

    public Color color() {
        return Color.GOLD;
    }

    public Plugin plugin() {
        return this.plugin;
    }

    public <P extends Plugin> P plugin(final Class<P> pluginClass) {
        return pluginClass.cast(this.plugin);
    }

    public abstract String developer();

    public net.kyori.adventure.text.Component sidebarCredit() {
        return MiniMessage.miniMessage().deserialize("<dark_aqua>@</dark_aqua><aqua>kuraiwtf</aqua>");
    }

    @Override
    public final String getId() {
        final var component = this.entity.getComponent(IdentifierComponent.class);
        if (component == null) {
            return "";
        }

        return component.getIdentifier().toString();
    }

    @Override
    public final String getName() {
        final var component = this.entity.getComponent(NameComponent.class);
        if (component == null) {
            return "";
        }

        return component.getName();
    }

    public final String getShortName() {
        final var component = this.entity.getComponent(ModuleShortNameComponent.class);
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
    public Collection<ModuleEvent> events() {
        return this.eventsView;
    }

    @Override
    public void registerEvent(final ModuleEvent event) {
        this.events.put(event.identifier(), event);
    }

    @Override
    public void unregisterEvent(final String identifier) {
        this.events.remove(identifier);
    }

    @Override
    public @Nullable ModuleEvent event(final String identifier) {
        return this.events.get(identifier);
    }

    public Collection<Object> commandHandlers() {
        return List.of();
    }

    public @Nullable ModuleMenu provideModuleMenu(final Player player) {
        return null;
    }

    public abstract ItemStack provideModuleIcon(final Player player);

    public void onRoles() {}
}
