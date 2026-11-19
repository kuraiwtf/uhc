package dev.kurai.uhc.profile.component;

import com.google.common.collect.Lists;
import dev.kurai.uhc.ecs.component.Component;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;

@NullMarked
public record ClaimComponent(Collection<ItemStack> items) implements Component {

    public ClaimComponent() {
        this(Lists.newArrayList());
    }
}
