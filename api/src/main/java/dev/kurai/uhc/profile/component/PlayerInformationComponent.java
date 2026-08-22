package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public record PlayerInformationComponent(
    Location lastLocation,
    ItemStack[] inventory,
    ItemStack[] armor,
    Collection<PotionEffect> effects,
    int fireTicks,
    float fallDistance)
    implements Component {}
