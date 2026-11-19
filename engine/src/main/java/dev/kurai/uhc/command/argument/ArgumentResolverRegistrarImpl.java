package dev.kurai.uhc.command.argument;

import com.google.common.collect.Maps;
import dev.kurai.uhc.command.argument.builtin.bukkit.OfflinePlayerArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.bukkit.PlayerArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.bukkit.WorldArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.java.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public final class ArgumentResolverRegistrarImpl implements ArgumentResolverRegistrar {

    private final BukkitAudiences bukkitAudiences;
    private final Map<Class<?>, ArgumentResolver<?>> argumentResolvers;
    private final Map<Class<?>, ArgumentResolver<?>> enumResolvers;

    public ArgumentResolverRegistrarImpl(final BukkitAudiences bukkitAudiences) {
        this.bukkitAudiences = bukkitAudiences;
        this.argumentResolvers = Maps.newConcurrentMap();
        this.enumResolvers = Maps.newConcurrentMap();
        this.loadArgumentResolvers();
    }

    private void loadArgumentResolvers() {
        this.registerArgumentResolver(String.class, new StringArgumentResolver());

        this.registerArgumentResolver(Float.class, new FloatArgumentResolver());
        this.registerArgumentResolver(float.class, new FloatArgumentResolver());

        this.registerArgumentResolver(Integer.class, new IntegerArgumentResolver());
        this.registerArgumentResolver(int.class, new IntegerArgumentResolver());

        this.registerArgumentResolver(Boolean.class, new BooleanArgumentResolver());
        this.registerArgumentResolver(boolean.class, new BooleanArgumentResolver());

        this.registerArgumentResolver(Double.class, new DoubleArgumentResolver());
        this.registerArgumentResolver(double.class, new DoubleArgumentResolver());

        this.registerArgumentResolver(Long.class, new LongArgumentResolver());
        this.registerArgumentResolver(long.class, new LongArgumentResolver());

        this.registerArgumentResolver(OfflinePlayer.class, new OfflinePlayerArgumentResolver(this.bukkitAudiences));
        this.registerArgumentResolver(Player.class, new PlayerArgumentResolver(this.bukkitAudiences));
        this.registerArgumentResolver(World.class, new WorldArgumentResolver(this.bukkitAudiences));
    }

    @Override
    public void registerArgumentResolver(final Class<?> clazz, final ArgumentResolver<?> resolver) {
        this.argumentResolvers.put(clazz, resolver);
    }

    @Override
    public <T> T resolveArgument(final Class<?> clazz, final CommandSender sender, final String argument) {
        final var resolver = this.findResolver(clazz);
        if (resolver == null) {
            throw new RuntimeException("There is no argument resolver for " + clazz.getName());
        }

        return (T) resolver.resolve(sender, argument);
    }

    @Override
    public Collection<String> complete(final Class<?> clazz, final CommandSender sender, final String argument) {
        final var resolver = this.findResolver(clazz);
        if (resolver == null) {
            return java.util.Collections.emptyList();
        }
        return resolver.complete(sender, argument);
    }

    private @Nullable ArgumentResolver<?> findResolver(final Class<?> clazz) {
        final var registered = this.argumentResolvers.get(clazz);
        if (registered != null) {
            return registered;
        }

        if (clazz.isEnum()) {
            return this.enumResolvers.computeIfAbsent(clazz, this::createEnumResolver);
        }

        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private ArgumentResolver<?> createEnumResolver(final Class<?> clazz) {
        return new EnumArgumentResolver(this.bukkitAudiences, clazz);
    }
}
