package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public final class DeathRecordComponent implements Component {

    private UUID killer;
    private long deathTime;

    private Location location;

    private ItemStack[] inventory;
    private ItemStack[] armor;

    private List<UUID> droppedItems;
}
