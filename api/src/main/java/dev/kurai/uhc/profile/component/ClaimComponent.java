package dev.kurai.uhc.profile.component;

import com.google.common.collect.Lists;
import dev.kurai.uhc.ecs.component.Component;
import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ClaimComponent implements Component {

  private final Collection<ItemStack> items;

  public ClaimComponent() {
    this.items = Lists.newArrayList();
  }

  public ClaimComponent(final Collection<ItemStack> items) {
    this.items = items;
  }

  public Collection<ItemStack> getItems() {
    return this.items;
  }
}
