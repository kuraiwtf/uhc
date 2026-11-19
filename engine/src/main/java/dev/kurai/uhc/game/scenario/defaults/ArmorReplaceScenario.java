package dev.kurai.uhc.game.scenario.defaults;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.game.scenario.AbstractScenario;
import dev.kurai.uhc.game.scenario.ScenarioCategory;
import dev.kurai.uhc.game.scenario.configuration.defaults.BooleanScenarioConfiguration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public final class ArmorReplaceScenario extends AbstractScenario implements Listener {

    private static final BooleanScenarioConfiguration HELMET_CONFIGURATION = new BooleanScenarioConfiguration("helmet");
    private static final BooleanScenarioConfiguration CHESTPLATE_CONFIGURATION =
            new BooleanScenarioConfiguration("chestplate");
    private static final BooleanScenarioConfiguration LEGGINGS_CONFIGURATION =
            new BooleanScenarioConfiguration("leggings");
    private static final BooleanScenarioConfiguration BOOTS_CONFIGURATION = new BooleanScenarioConfiguration("boots");

    public ArmorReplaceScenario(final UltraHardcoreAPI ultraHardcore) {
        super("armor_replace", "Armor Replace", ultraHardcore, ScenarioCategory.COMBAT);
        this.registerConfiguration(HELMET_CONFIGURATION);
        this.registerConfiguration(CHESTPLATE_CONFIGURATION);
        this.registerConfiguration(LEGGINGS_CONFIGURATION);
        this.registerConfiguration(BOOTS_CONFIGURATION);
    }

    @Override
    public List<String> provideLore() {
        return Lists.newArrayList("Effectuer un Clic-Droit avec une", "pièce d'armure la remplace.");
    }

    @Override
    public ItemStack provideIcon() {
        return new ItemStack(Material.IRON_CHESTPLATE);
    }

    @EventHandler
    public void onArmorInteract(final @NonNull PlayerInteractEvent event) {
        if (!event.hasItem() || !event.getAction().name().contains("RIGHT")) {
            return;
        }

        final var item = event.getItem();
        final var piece = this.retrieveArmorPiece(item.getType());
        if (piece == null) {
            return;
        }

        final BooleanScenarioConfiguration config;
        config = switch (piece) {
            case HELMET -> HELMET_CONFIGURATION;
            case CHESTPLATE -> CHESTPLATE_CONFIGURATION;
            case LEGGINGS -> LEGGINGS_CONFIGURATION;
            case BOOTS -> BOOTS_CONFIGURATION;
        };

        if (!config.getValue()) {
            return;
        }

        final var player = event.getPlayer();
        piece.getApply().accept(player, item.clone());
        event.setCancelled(true);
        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1f, 1f);
    }

    @Contract(pure = true)
    public @Nullable ArmorPiece retrieveArmorPiece(final @NonNull Material material) {
        return switch (material) {
            case LEATHER_HELMET, CHAINMAIL_HELMET, IRON_HELMET, GOLD_HELMET, DIAMOND_HELMET -> ArmorPiece.HELMET;
            case LEATHER_CHESTPLATE, CHAINMAIL_CHESTPLATE, IRON_CHESTPLATE, GOLD_CHESTPLATE, DIAMOND_CHESTPLATE ->
                ArmorPiece.CHESTPLATE;
            case LEATHER_LEGGINGS, CHAINMAIL_LEGGINGS, IRON_LEGGINGS, GOLD_LEGGINGS, DIAMOND_LEGGINGS ->
                ArmorPiece.LEGGINGS;
            case LEATHER_BOOTS, CHAINMAIL_BOOTS, IRON_BOOTS, GOLD_BOOTS, DIAMOND_BOOTS -> ArmorPiece.BOOTS;
            default -> null;
        };
    }

    public enum ArmorPiece {
        HELMET((player, itemStack) -> {
            final var helmet = player.getInventory().getHelmet();
            player.getInventory().setHelmet(itemStack);
            player.getInventory().setItemInHand(helmet == null ? null : helmet.clone());
        }),
        CHESTPLATE((player, itemStack) -> {
            final var chestplate = player.getInventory().getChestplate();
            player.getInventory().setChestplate(itemStack);
            player.getInventory().setItemInHand(chestplate == null ? null : chestplate.clone());
        }),
        LEGGINGS((player, itemStack) -> {
            final var leggings = player.getInventory().getLeggings();
            player.getInventory().setLeggings(itemStack);
            player.getInventory().setItemInHand(leggings == null ? null : leggings.clone());
        }),
        BOOTS((player, itemStack) -> {
            final var boots = player.getInventory().getBoots();
            player.getInventory().setBoots(itemStack);
            player.getInventory().setItemInHand(boots == null ? null : boots.clone());
        }),
        ;

        private final BiConsumer<Player, ItemStack> apply;

        ArmorPiece(final BiConsumer<Player, ItemStack> apply) {
            this.apply = apply;
        }

        public BiConsumer<Player, ItemStack> getApply() {
            return this.apply;
        }

        public @NonNull String getId() {
            return this.name().toLowerCase();
        }
    }
}
