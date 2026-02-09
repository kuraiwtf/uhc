package dev.kurai.uhc.game.configuration.inventory;

import static dev.kurai.uhc.util.api.option.Option.option;
import static net.kyori.adventure.key.Key.key;

import dev.kurai.uhc.util.api.option.Option;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class InventoryConfiguration {

  public static final Option<ItemStack @NotNull []> INVENTORY_CONTENT_OPTION =
      option(key("inventory_content"), new ItemStack[36]);

  public static final Option<ItemStack @NotNull []> INVENTORY_ARMOR_OPTION =
      option(key("inventory_armor_content"), new ItemStack[4]);
}
