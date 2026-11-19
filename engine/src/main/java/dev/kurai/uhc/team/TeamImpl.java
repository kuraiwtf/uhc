package dev.kurai.uhc.team;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;

@NullMarked
final class TeamImpl implements Team {

    private final int id;
    private final TeamStyle style;
    private final Set<UUID> members = Sets.newLinkedHashSet();
    private final BooleanSupplier inventoryEnabled;

    private UUID leader;
    private @Nullable Inventory inventory;

    TeamImpl(final int id, final TeamStyle style, final UUID leader, final BooleanSupplier inventoryEnabled) {
        this.id = id;
        this.style = style;
        this.leader = leader;
        this.inventoryEnabled = inventoryEnabled;
        this.members.add(leader);
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.style.displayName();
    }

    @Override
    public TeamStyle style() {
        return this.style;
    }

    @Override
    public UUID leader() {
        return this.leader;
    }

    void leader(final UUID leader) {
        this.leader = leader;
    }

    @Override
    public Collection<UUID> members() {
        return Set.copyOf(this.members);
    }

    Set<UUID> mutableMembers() {
        return this.members;
    }

    @Override
    public boolean contains(final UUID uniqueId) {
        return this.members.contains(uniqueId);
    }

    @Override
    public @Nullable Inventory inventory() {
        if (!this.inventoryEnabled.getAsBoolean()) {
            return null;
        }

        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, 27, "Inventaire d'équipe");
        }
        return this.inventory;
    }
}
