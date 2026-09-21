package dev.kurai.uhc.power;

import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PowerService {

  Collection<PowerDecorator> decorators();

  void registerDecorator(final PowerDecorator decorator);

  ItemStack provideIcon(final AbstractItemPower power, final Player player);
}
