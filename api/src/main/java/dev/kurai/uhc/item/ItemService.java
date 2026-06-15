package dev.kurai.uhc.item;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemService {

  Collection< CustomItem> findAllBy(
      final  Predicate< CustomItem> filter,
      final  Collector<? super CustomItem, ?, ? extends Collection< CustomItem>>
              collector);

  default Collection< CustomItem> findAllBy(
      final  Predicate< CustomItem> filter) {
    return this.findAllBy(filter, Collectors.toList());
  }

  default Collection< CustomItem> findAll() {
    return this.findAllBy(_ -> true);
  }

  default Collection< CustomItem> findAllByHostOnly() {
    return this.findAllBy(CustomItem::isHostOnly);
  }

  default Collection< CustomItem> findAllBySpectatorOnly() {
    return this.findAllBy(CustomItem::isSpectatorOnly);
  }

  void registerItem(final  CustomItem item);

  default void registerItems(final CustomItem  ... items) {
    for (final var item : items) {
      this.registerItem(item);
    }
  }

  void unregisterItem(final  CustomItem item);

  default void unregisterItems(final CustomItem  ... items) {
    for (final var item : items) {
      this.unregisterItem(item);
    }
  }

  
  Optional<CustomItem> findByIdentifier(final  String identifier);

  Optional<CustomItem> findByIcon(final  Player player, final  ItemStack icon);

  <T extends CustomItem>  Optional<T> findByClass(final  Class<T> clazz);
}
