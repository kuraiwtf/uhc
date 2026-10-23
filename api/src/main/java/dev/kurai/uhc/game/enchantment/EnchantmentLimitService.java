package dev.kurai.uhc.game.enchantment;

import java.util.OptionalInt;
import java.util.UUID;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EnchantmentLimitService {

  OptionalInt limit(EnchantmentLimitType type, Enchantment enchantment);

  void limit(EnchantmentLimitType type, Enchantment enchantment, int level);

  void resetLimit(EnchantmentLimitType type, Enchantment enchantment);

  OptionalInt limit(UUID playerId, EnchantmentLimitType type, Enchantment enchantment);

  void limit(UUID playerId, EnchantmentLimitType type, Enchantment enchantment, int level);

  void resetLimit(UUID playerId, EnchantmentLimitType type, Enchantment enchantment);

  OptionalInt effectiveLimit(UUID playerId, org.bukkit.Material material, Enchantment enchantment);

  boolean sanitize(UUID playerId, ItemStack item);
}
