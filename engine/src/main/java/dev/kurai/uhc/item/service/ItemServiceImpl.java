package dev.kurai.uhc.item.service;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.item.CustomItem;
import dev.kurai.uhc.item.builtin.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class ItemServiceImpl implements ItemService {

  private final Map<@NotNull String, @NotNull CustomItem> registeredItems;

  public ItemServiceImpl(final @NotNull UltraHardcoreAPI ultraHardcore) {
    this.registeredItems = Maps.newHashMap();
    this.registerItems(
        new ScenarioItem(ultraHardcore),
        new MumbleItem(ultraHardcore),
        new ConfigurationItem(ultraHardcore),
        new JumpItem(),
        new LogoutItem());
  }

  @Override
  public @NotNull @Unmodifiable Collection<@NotNull CustomItem> findAllBy(
      final @NotNull Predicate<@NotNull CustomItem> filter,
      final @NotNull Collector<? super CustomItem, ?, ? extends Collection<@NotNull CustomItem>>
              collector) {
    return this.registeredItems.values().stream().filter(filter).collect(collector);
  }

  @Override
  public void registerItem(final @NotNull CustomItem item) {
    this.registeredItems.put(item.getIdentifier(), item);
  }

  @Override
  public void unregisterItem(final @NotNull CustomItem item) {
    this.registeredItems.remove(item.getIdentifier());
  }

  @Override
  public @NotNull Optional<CustomItem> findByIdentifier(final @NotNull String identifier) {
    return Optional.ofNullable(this.registeredItems.get(identifier));
  }

  @Override
  public @NotNull Optional<CustomItem> findByIcon(
      final @NotNull Player player, final @NotNull ItemStack icon) {
    return this.registeredItems.values().stream()
        .filter(customItem -> customItem.provideIcon(player).isSimilar(icon))
        .findFirst();
  }

  @Override
  public @NotNull <T extends CustomItem> Optional<T> findByClass(final @NotNull Class<T> clazz) {
    return this.registeredItems.values().stream()
        .filter(customItem -> customItem.getClass() == clazz)
        .map(clazz::cast)
        .findFirst();
  }
}
