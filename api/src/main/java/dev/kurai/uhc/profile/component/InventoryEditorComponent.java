package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import org.bukkit.inventory.ItemStack;

public record InventoryEditorComponent(ItemStack[] savedInventory, ItemStack[] savedArmor)
    implements Component {}
