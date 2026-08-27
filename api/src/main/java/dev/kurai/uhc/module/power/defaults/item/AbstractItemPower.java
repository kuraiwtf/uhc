package dev.kurai.uhc.module.power.defaults.item;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItemPower extends AbstractPower {

  protected AbstractItemPower(
      final String identifier,
      final String name,
      final UUID owner,
      final UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
  }

  public abstract ItemStack provideIcon(final Player player);

  public ItemStack getIcon(final Player player) {
    final var lore = Lists.<String>newArrayList();
    lore.add("");
    lore.addAll(this.lore());
    lore.add("");

    return new ItemBuilder(this.provideIcon(player))
        .name("&8&l»%s &l%s&8 &l«".formatted(this.getColor().asBukkitColor(), this.name))
        .lore(lore)
        .asItemStack();
  }

  public boolean shouldDistributePower(final Player player) {
    return true;
  }
}
