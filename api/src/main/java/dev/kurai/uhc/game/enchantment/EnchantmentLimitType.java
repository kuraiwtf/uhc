package dev.kurai.uhc.game.enchantment;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public enum EnchantmentLimitType {
    GLOBAL("&d&lGlobale", "&d", Material.ENCHANTED_BOOK, "global"),
    IRON("&7&lFer", "&7", Material.IRON_CHESTPLATE, "fer"),
    DIAMOND("&b&lDiamant", "&b", Material.DIAMOND_CHESTPLATE, "diamant");

    private final String displayName;
    private final String color;
    private final Material icon;
    private final String alias;

    EnchantmentLimitType(final String displayName, final String color, final Material icon, final String alias) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.alias = alias;
    }

    /**
     * Resolves the limit category an item belongs to, based on its material.
     *
     * @return {@link #IRON} or {@link #DIAMOND} for iron/diamond equipment, {@code null} otherwise
     */
    public static @Nullable EnchantmentLimitType of(final @Nullable Material material) {
        if (material == null) {
            return null;
        }

        final var name = material.name();
        if (name.startsWith("IRON_")) {
            return IRON;
        }
        return name.startsWith("DIAMOND_") ? DIAMOND : null;
    }

    public String displayName() {
        return this.displayName;
    }

    public String color() {
        return this.color;
    }

    public Material icon() {
        return this.icon;
    }

    public String alias() {
        return this.alias;
    }
}
