package dev.kurai.uhc.item;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.item.builtin.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public final class ItemServiceImpl implements ItemService {

  private final Map<String, CustomItem> registeredItems;
  private final Int2ObjectMap<WaitingItem> waitingItems;

  public ItemServiceImpl(final UltraHardcoreAPI ultraHardcore) {
    this.registeredItems = Maps.newHashMap();
    this.waitingItems = new Int2ObjectOpenHashMap<>();

    this.registerWaitingItem(0, new ScenarioItem(ultraHardcore));
    this.registerWaitingItem(4, new ConfigurationItem(ultraHardcore));
    this.registerWaitingItem(7, new JumpItem());
    this.registerWaitingItem(8, new LogoutItem());
  }

  @Override
  public @Unmodifiable Collection<CustomItem> findAllBy(
      final Predicate<CustomItem> filter,
      final Collector<? super CustomItem, ?, ? extends Collection<CustomItem>> collector) {
    return this.registeredItems.values().stream().filter(filter).collect(collector);
  }

  @Override
  public @Unmodifiable Collection<WaitingItem> findWaitingItems() {
    return List.copyOf(this.waitingItems.values());
  }

  @Override
  public void registerWaitingItem(final int slot, final CustomItem item) {
    this.waitingItems.put(slot, new WaitingItem(slot, item));
    this.registerItem(item);
  }

  @Override
  public WaitingItem findWaitingItem(final int slot) {
    return this.waitingItems.get(slot);
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
