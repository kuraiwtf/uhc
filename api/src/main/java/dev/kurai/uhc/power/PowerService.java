package dev.kurai.uhc.power;

import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface PowerService {

    Collection<PowerDecorator> decorators();

    void registerDecorator(final PowerDecorator decorator);

    ItemStack provideIcon(final AbstractItemPower power, final Player player);
}
