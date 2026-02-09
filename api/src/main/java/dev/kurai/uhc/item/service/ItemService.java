package dev.kurai.uhc.item.service;

import dev.kurai.uhc.item.CustomItem;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemService {

  Collection<@NotNull CustomItem> findAllBy(
      final @NotNull Predicate<@NotNull CustomItem> filter,
      final @NotNull Collector<? super CustomItem, ?, ? extends Collection<@NotNull CustomItem>>
              collector);

  default Collection<@NotNull CustomItem> findAllBy(
      final @NotNull Predicate<@NotNull CustomItem> filter) {
    return this.findAllBy(filter, Collectors.toList());
  }

  default Collection<@NotNull CustomItem> findAll() {
    return this.findAllBy(_ -> true);
  }

  default Collection<@NotNull CustomItem> findAllByHostOnly() {
    return this.findAllBy(CustomItem::isHostOnly);
  }

  default Collection<@NotNull CustomItem> findAllBySpectatorOnly() {
    return this.findAllBy(CustomItem::isSpectatorOnly);
  }

  void registerItem(final @NotNull CustomItem item);

  default void registerItems(final CustomItem @NotNull ... items) {
    for (final var item : items) {
      this.registerItem(item);
    }
  }

  void unregisterItem(final @NotNull CustomItem item);

  default void unregisterItems(final CustomItem @NotNull ... items) {
    for (final var item : items) {
      this.unregisterItem(item);
    }
  }

  @NotNull
  Optional<CustomItem> findByIdentifier(final @NotNull String identifier);

  Optional<CustomItem> findByIcon(final @NotNull Player player, final @NotNull ItemStack icon);

  <T extends CustomItem> @NotNull Optional<T> findByClass(final @NotNull Class<T> clazz);
}
