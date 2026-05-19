package dev.kurai.uhc.command.argument;

import static dev.kurai.uhc.command.argument.builtin.JavaArgumentResolvers.*;
import static dev.kurai.uhc.command.argument.builtin.JavaArgumentResolvers.BOOLEAN_RESOLVER;
import static dev.kurai.uhc.command.argument.builtin.JavaArgumentResolvers.DOUBLE_RESOLVER;
import static dev.kurai.uhc.command.argument.builtin.JavaArgumentResolvers.LONG_RESOLVER;

import com.google.common.collect.Maps;
import dev.kurai.uhc.command.argument.builtin.bukkit.OfflinePlayerArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.bukkit.PlayerArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.bukkit.WorldArgumentResolver;
import java.util.Collection;
import java.util.Map;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ArgumentResolverRegistrarImpl implements ArgumentResolverRegistrar {

  private final BukkitAudiences bukkitAudiences;
  private final Map<@NotNull Class<?>, @NotNull ArgumentResolver<?>> argumentResolvers;

  public ArgumentResolverRegistrarImpl(final @NotNull BukkitAudiences bukkitAudiences) {
    this.bukkitAudiences = bukkitAudiences;
    this.argumentResolvers = Maps.newConcurrentMap();
    this.loadArgumentResolvers();
  }

  private void loadArgumentResolvers() {
    this.registerArgumentResolver(String.class, STRING_RESOLVER);

    this.registerArgumentResolver(Integer.class, INTEGER_RESOLVER);
    this.registerArgumentResolver(int.class, INTEGER_RESOLVER);

    this.registerArgumentResolver(Boolean.class, BOOLEAN_RESOLVER);
    this.registerArgumentResolver(boolean.class, BOOLEAN_RESOLVER);

    this.registerArgumentResolver(Double.class, DOUBLE_RESOLVER);
    this.registerArgumentResolver(double.class, DOUBLE_RESOLVER);

    this.registerArgumentResolver(Long.class, LONG_RESOLVER);
    this.registerArgumentResolver(long.class, LONG_RESOLVER);

    this.registerArgumentResolver(
        OfflinePlayer.class, new OfflinePlayerArgumentResolver(this.bukkitAudiences));
    this.registerArgumentResolver(Player.class, new PlayerArgumentResolver(this.bukkitAudiences));
    this.registerArgumentResolver(World.class, new WorldArgumentResolver(this.bukkitAudiences));
  }

  @Override
  public void registerArgumentResolver(
      final @NotNull Class<?> clazz, final @NotNull ArgumentResolver<?> resolver) {
    this.argumentResolvers.put(clazz, resolver);
  }

  @Override
  public <T> T resolveArgument(
      final @NotNull Class<?> clazz,
      final @NotNull CommandSender sender,
      final @NotNull String argument) {
    if (!this.argumentResolvers.containsKey(clazz)) {
      throw new RuntimeException("There is no argument resolver for " + clazz.getName());
    }

    return (T) this.argumentResolvers.get(clazz).resolve(sender, argument);
  }

  @Override
  public Collection<@NotNull String> complete(
      final @NotNull Class<?> clazz,
      final @NotNull CommandSender sender,
      final @NotNull String argument) {
    final var resolver = this.argumentResolvers.get(clazz);
    if (resolver == null) {
      return java.util.Collections.emptyList();
    }
    return resolver.complete(sender, argument);
  }
}
