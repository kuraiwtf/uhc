package dev.kurai.uhc.game.enchantment;

import java.util.OptionalInt;
import java.util.UUID;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the maximum level of each enchantment, per limit type ({@link EnchantmentLimitType}), for
 * the whole game and as per-player overrides. An absent limit means the vanilla maximum, and a
 * limit of {@code 0} forbids the enchantment.
 */
@NullMarked
public interface EnchantmentLimitService {

  OptionalInt limit(EnchantmentLimitType type, Enchantment enchantment);

  void limit(EnchantmentLimitType type, Enchantment enchantment, int level);

  void resetLimit(EnchantmentLimitType type, Enchantment enchantment);

  OptionalInt limit(UUID playerId, EnchantmentLimitType type, Enchantment enchantment);

  void limit(UUID playerId, EnchantmentLimitType type, Enchantment enchantment, int level);

  void resetLimit(UUID playerId, EnchantmentLimitType type, Enchantment enchantment);

  /**
   * Resolves the limit applying to {@code enchantment} on {@code material} for the player. The
   * player's own limits win over the game's, and within each the item's type wins over the global
   * limit.
   */
  OptionalInt effectiveLimit(UUID playerId, org.bukkit.Material material, Enchantment enchantment);

  /**
   * Lowers (or removes) the enchantments of {@code item} that exceed the player's limits.
   *
   * @return whether the item was modified
   */
  boolean sanitize(UUID playerId, ItemStack item);
}
