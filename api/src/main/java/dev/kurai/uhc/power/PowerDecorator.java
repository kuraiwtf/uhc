package dev.kurai.uhc.power;

import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PowerDecorator {

  ItemStack decorate(final Player player, final AbstractItemPower power, final ItemStack stack);
}
