package dev.kurai.uhc.power;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Lists;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PowerServiceImpl implements PowerService {

  private final Collection<PowerDecorator> decorators;

  public PowerServiceImpl() {
    this.decorators = new CopyOnWriteArrayList<>();
    this.registerDecorator(
        (_, power, stack) -> {
          final var lore = Lists.<String>newArrayList();
          lore.add("");
          lore.addAll(power.lore());
          lore.add("");

          return new ItemBuilder(stack)
              .name("§8§l»" + power.getColor().asBukkitColor() + " §l" + power.getName() + "§8 §l«")
              .lore(lore)
              .asItemStack();
        });
    this.registerDecorator(
        (player, power, stack) ->
            new ItemBuilder(stack).lunarTag("glint", power.provideGlint(player)).asItemStack());
  }

  @Override
  public Collection<PowerDecorator> decorators() {
    return List.copyOf(this.decorators);
  }

  @Override
  public void registerDecorator(final PowerDecorator decorator) {
    this.decorators.add(requireNonNull(decorator, "decorator cannot be null"));
  }

  @Override
  public ItemStack provideIcon(final AbstractItemPower power, final Player player) {
    ItemStack icon = power.provideIcon(player);
    for (final PowerDecorator decorator : this.decorators) {
      icon =
          requireNonNull(decorator.decorate(player, power, icon), "decorator cannot return null");
    }
    return icon;
  }
}
