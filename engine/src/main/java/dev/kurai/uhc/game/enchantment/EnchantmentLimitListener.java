package dev.kurai.uhc.game.enchantment;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

@NullMarked
public final class EnchantmentLimitListener implements Listener {

    private static final int ANVIL_RESULT_SLOT = 2;

    private final EnchantmentLimitService limitService;

    public EnchantmentLimitListener(final EnchantmentLimitService limitService) {
        this.limitService = limitService;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEnchant(final EnchantItemEvent event) {
        final var playerId = event.getEnchanter().getUniqueId();
        final var material = event.getItem().getType();

        for (final Map.Entry<Enchantment, Integer> entry :
                Map.copyOf(event.getEnchantsToAdd()).entrySet()) {
            final var limit = this.limitService.effectiveLimit(playerId, material, entry.getKey());
            if (limit.isEmpty() || entry.getValue() <= limit.getAsInt()) {
                continue;
            }

            if (limit.getAsInt() > 0) {
                event.getEnchantsToAdd().put(entry.getKey(), limit.getAsInt());
            } else {
                event.getEnchantsToAdd().remove(entry.getKey());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAnvilResult(final InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.ANVIL
                || event.getRawSlot() != ANVIL_RESULT_SLOT
                || !(event.getWhoClicked() instanceof final Player player)) {
            return;
        }

        final ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() == Material.AIR) {
            return;
        }

        this.limitService.sanitize(player.getUniqueId(), result);
    }
}
