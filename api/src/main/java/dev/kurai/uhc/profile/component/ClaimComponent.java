package dev.kurai.uhc.profile.component;

import com.google.common.collect.Lists;
import dev.kurai.uhc.ecs.component.Component;
import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ClaimComponent(Collection<ItemStack> items) implements Component {

  public ClaimComponent() {
    this(Lists.newArrayList());
  }
}
