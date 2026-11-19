package dev.kurai.uhc.menu.rules.enchantment;

import dev.kurai.uhc.game.enchantment.EnchantmentLimitService;
import dev.kurai.uhc.game.enchantment.EnchantmentLimitType;
import dev.kurai.uhc.menu.template.BackTemplate;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.util.ItemBuilder;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.OptionalInt;

import static dev.kurai.uhc.util.CC.SQUARE;

@NullMarked
public final class EnchantmentLimitViewMenu extends Menu {

    private static final int[] SLOTS = {
        10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43
    };

    private static final Map<EnchantmentLimitType, Material> REPRESENTATIVES = Map.of(
            EnchantmentLimitType.GLOBAL, Material.BOOK,
            EnchantmentLimitType.IRON, Material.IRON_SWORD,
            EnchantmentLimitType.DIAMOND, Material.DIAMOND_SWORD);

    private final EnchantmentLimitService limitService;

    public EnchantmentLimitViewMenu(final Player player, final EnchantmentLimitService limitService) {
        super("Vos limites d'enchantement", MenuSize.SIX, player);
        this.limitService = limitService;
    }

    @Override
    public void setup(final BackgroundLayer back, final ForegroundLayer front) {
        this.apply(new BorderTemplate(DyeColor.PURPLE.getData()));
        this.apply(new BackTemplate(this.getPreviousMenu()));

        final var enchantments = Enchantment.values();
        for (int i = 0; i < enchantments.length && i < SLOTS.length; i++) {
            front.set(SLOTS[i], new dev.kurai.uhc.menu.button.ItemButton(this.icon(enchantments[i])));
        }
    }

    private org.bukkit.inventory.ItemStack icon(final Enchantment enchantment) {
        final var lore = new java.util.ArrayList<String>();
        lore.add("");
        for (final var type : EnchantmentLimitType.values()) {
            lore.add(type.color()
                    + " "
                    + SQUARE
                    + "&f "
                    + type.alias().substring(0, 1).toUpperCase()
                    + type.alias().substring(1)
                    + ": "
                    + this.format(enchantment, type));
        }
        lore.add("");

        return new ItemBuilder(Material.ENCHANTED_BOOK)
                .name("&d&l"
                        + EnchantmentLimitListMenu.NAMES.getOrDefault(enchantment.getName(), enchantment.getName()))
                .lore(lore)
                .asItemStack();
    }

    private String format(final Enchantment enchantment, final EnchantmentLimitType type) {
        final OptionalInt limit = this.limitService.effectiveLimit(
                this.getPlayer().getUniqueId(), REPRESENTATIVES.get(type), enchantment);
        if (limit.isEmpty()) {
            return "&aVanilla &7(" + enchantment.getMaxLevel() + ")";
        }
        return limit.getAsInt() == 0 ? "&cInterdit" : "&e" + limit.getAsInt();
    }
}
