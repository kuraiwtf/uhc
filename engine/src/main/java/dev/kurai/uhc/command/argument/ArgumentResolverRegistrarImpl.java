package dev.kurai.uhc.command.argument;

import com.google.common.collect.Maps;
import dev.kurai.uhc.command.argument.builtin.bukkit.OfflinePlayerArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.bukkit.PlayerArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.bukkit.WorldArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.java.BooleanArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.java.DoubleArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.java.FloatArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.java.IntegerArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.java.LongArgumentResolver;
import dev.kurai.uhc.command.argument.builtin.java.StringArgumentResolver;
import java.util.Collection;
import java.util.Map;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ArgumentResolverRegistrarImpl implements ArgumentResolverRegistrar {

  private final BukkitAudiences bukkitAudiences;
  private final Map<Class<?>, ArgumentResolver<?>> argumentResolvers;

  public ArgumentResolverRegistrarImpl(final BukkitAudiences bukkitAudiences) {
    this.bukkitAudiences = bukkitAudiences;
    this.argumentResolvers = Maps.newConcurrentMap();
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

    this.registerArgumentResolver(
        OfflinePlayer.class, new OfflinePlayerArgumentResolver(this.bukkitAudiences));
    this.registerArgumentResolver(Player.class, new PlayerArgumentResolver(this.bukkitAudiences));
    this.registerArgumentResolver(World.class, new WorldArgumentResolver(this.bukkitAudiences));
  }

  @Override
  public void registerArgumentResolver(final Class<?> clazz, final ArgumentResolver<?> resolver) {
    this.argumentResolvers.put(clazz, resolver);
  }

  @Override
  public <T> T resolveArgument(
      final Class<?> clazz, final CommandSender sender, final String argument) {
    if (!this.argumentResolvers.containsKey(clazz)) {
      throw new RuntimeException("There is no argument resolver for " + clazz.getName());
    }

    return (T) (this.argumentResolvers.get(clazz)).resolve(sender, argument);
  }

  @Override
  public Collection<String> complete(
      final Class<?> clazz, final CommandSender sender, final String argument) {
    final var resolver = this.argumentResolvers.get(clazz);
    if (resolver == null) {
      return java.util.Collections.emptyList();
    }
    return resolver.complete(sender, argument);
  }
}
