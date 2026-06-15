package dev.kurai.uhc.item;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.item.builtin.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public final class ItemServiceImpl implements ItemService {

  private final Map<String, CustomItem> registeredItems;

  public ItemServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.registeredItems = Maps.newHashMap();
    this.registerItems(
        new ScenarioItem(ultraHardcore),
        new MumbleItem(ultraHardcore),
        new ConfigurationItem(ultraHardcore),
        new JumpItem(),
        new LogoutItem());
  }

  @Override
  public @Unmodifiable Collection<CustomItem> findAllBy(
      final Predicate<CustomItem> filter,
      final Collector<? super CustomItem, ?, ? extends Collection<CustomItem>> collector) {
    return this.registeredItems.values().stream().filter(filter).collect(collector);
  }

  @Override
  public void registerItem(final CustomItem item) {
    this.registeredItems.put(item.getIdentifier(), item);
  }

  @Override
  public void unregisterItem(final CustomItem item) {
    this.registeredItems.remove(item.getIdentifier());
  }

  @Override
  public Optional<CustomItem> findByIdentifier(final String identifier) {
    return Optional.ofNullable(this.registeredItems.get(identifier));
  }

  @Override
  public Optional<CustomItem> findByIcon(final Player player, final ItemStack icon) {
    return this.registeredItems.values().stream()
        .filter(customItem -> customItem.provideIcon(player).isSimilar(icon))
        .findFirst();
  }

  @Override
  public <T extends CustomItem> Optional<T> findByClass(final Class<T> clazz) {
    return this.registeredItems.values().stream()
        .filter(customItem -> customItem.getClass() == clazz)
        .map(clazz::cast)
        .findFirst();
  }
}
