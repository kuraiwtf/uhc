package dev.kurai.uhc.game.enchantment;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;

@NullMarked
public final class EnchantmentLimitServiceImpl implements EnchantmentLimitService {

    private final Map<EnchantmentLimitType, Map<Enchantment, Integer>> gameLimits;
    private final Map<UUID, Map<EnchantmentLimitType, Map<Enchantment, Integer>>> playerLimits;

    public EnchantmentLimitServiceImpl() {
        this.gameLimits = Maps.newConcurrentMap();
        this.playerLimits = Maps.newConcurrentMap();
    }

    private static OptionalInt get(
            final Map<EnchantmentLimitType, Map<Enchantment, Integer>> limits,
            final EnchantmentLimitType type,
            final Enchantment enchantment) {
        final var byEnchantment = limits.get(type);
        final var level = byEnchantment == null ? null : byEnchantment.get(enchantment);
        return level == null ? OptionalInt.empty() : OptionalInt.of(level);
    }

    private static void set(
            final Map<EnchantmentLimitType, Map<Enchantment, Integer>> limits,
            final EnchantmentLimitType type,
            final Enchantment enchantment,
            final int level) {
        limits.computeIfAbsent(type, key -> Maps.newConcurrentMap()).put(enchantment, Math.max(0, level));
    }

    private static void remove(
            final Map<EnchantmentLimitType, Map<Enchantment, Integer>> limits,
            final EnchantmentLimitType type,
            final Enchantment enchantment) {
        final var byEnchantment = limits.get(type);
        if (byEnchantment != null) {
            byEnchantment.remove(enchantment);
        }
    }

    @Override
    public OptionalInt limit(final EnchantmentLimitType type, final Enchantment enchantment) {
        return get(this.gameLimits, type, enchantment);
    }

    @Override
    public void limit(final EnchantmentLimitType type, final Enchantment enchantment, final int level) {
        set(this.gameLimits, type, enchantment, level);
    }

    @Override
    public void resetLimit(final EnchantmentLimitType type, final Enchantment enchantment) {
        remove(this.gameLimits, type, enchantment);
    }

    @Override
    public OptionalInt limit(final UUID playerId, final EnchantmentLimitType type, final Enchantment enchantment) {
        final var limits = this.playerLimits.get(playerId);
        return limits == null ? OptionalInt.empty() : get(limits, type, enchantment);
    }

    @Override
    public void limit(
            final UUID playerId, final EnchantmentLimitType type, final Enchantment enchantment, final int level) {
        set(this.playerLimits.computeIfAbsent(playerId, id -> Maps.newConcurrentMap()), type, enchantment, level);
    }

    @Override
    public void resetLimit(final UUID playerId, final EnchantmentLimitType type, final Enchantment enchantment) {
        final var limits = this.playerLimits.get(playerId);
        if (limits != null) {
            remove(limits, type, enchantment);
        }
    }

    @Override
    public OptionalInt effectiveLimit(final UUID playerId, final Material material, final Enchantment enchantment) {
        final var itemType = EnchantmentLimitType.of(material);

        if (itemType != null) {
            final var playerLimit = this.limit(playerId, itemType, enchantment);
            if (playerLimit.isPresent()) {
                return playerLimit;
            }
        }

        final var playerGlobal = this.limit(playerId, EnchantmentLimitType.GLOBAL, enchantment);
        if (playerGlobal.isPresent()) {
            return playerGlobal;
        }

        if (itemType != null) {
            final var gameLimit = this.limit(itemType, enchantment);
            if (gameLimit.isPresent()) {
                return gameLimit;
            }
        }

        return this.limit(EnchantmentLimitType.GLOBAL, enchantment);
    }

    @Override
    public boolean sanitize(final UUID playerId, final ItemStack item) {
        final var material = item.getType();
        var changed = false;

        for (final var entry : Map.copyOf(item.getEnchantments()).entrySet()) {
            final var limit = this.effectiveLimit(playerId, material, entry.getKey());
            if (limit.isEmpty() || entry.getValue() <= limit.getAsInt()) {
                continue;
            }

            item.removeEnchantment(entry.getKey());
            if (limit.getAsInt() > 0) {
                item.addUnsafeEnchantment(entry.getKey(), limit.getAsInt());
            }
            changed = true;
        }

        if (item.getItemMeta() instanceof final EnchantmentStorageMeta meta) {
            var metaChanged = false;
            for (final var entry : Map.copyOf(meta.getStoredEnchants()).entrySet()) {
                final var limit = this.effectiveLimit(playerId, material, entry.getKey());
                if (limit.isEmpty() || entry.getValue() <= limit.getAsInt()) {
                    continue;
                }

                meta.removeStoredEnchant(entry.getKey());
                if (limit.getAsInt() > 0) {
                    meta.addStoredEnchant(entry.getKey(), limit.getAsInt(), true);
                }
                metaChanged = true;
            }

            if (metaChanged) {
                item.setItemMeta(meta);
                changed = true;
            }
        }

        return changed;
    }
}
