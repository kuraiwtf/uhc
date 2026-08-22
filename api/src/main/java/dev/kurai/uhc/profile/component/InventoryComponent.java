package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import org.bukkit.inventory.ItemStack;

public record InventoryComponent(ItemStack[] contents, ItemStack[] armor) implements Component {}
