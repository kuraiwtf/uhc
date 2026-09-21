package dev.kurai.uhc.module.power.defaults.item;

import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItemPower extends AbstractPower {

  public static final String NBT_TAG = "PowerIdentifier";

  protected AbstractItemPower(
      final String identifier,
      final String name,
      final UUID owner,
      final UltraHardcoreAPI ultraHardcore) {
    super(identifier, name, owner, ultraHardcore);
  }

  public abstract ItemStack provideIcon(final Player player);

  public ItemStack getIcon(final Player player) {
    return new ItemBuilder(this.ultraHardcore.powerService().provideIcon(this, player))
        .tag(NBT_TAG, this.getId())
        .asItemStack();
  }

  public Object provideGlint(final Player player) {
    final CooldownPowerRestriction cooldown =
        this.findRestriction(CooldownPowerRestriction.class, "cooldown");
    if (cooldown != null && cooldown.restrictsPower(this, player)) {
      return "#AA0000";
    }

    return this.useColorForGlint() ? this.getColor().asJavaColor().getRGB() : "";
  }

  public boolean useColorForGlint() {
    return false;
  }

  public boolean shouldDistributePower(final Player player) {
    return true;
  }

  public boolean isSimilar(final ItemStack itemStack) {
    return itemStack != null
        && itemStack.getType() != Material.AIR
        && ItemBuilder.hasTag(itemStack, NBT_TAG)
        && ItemBuilder.getTag(itemStack, NBT_TAG).equals(this.getId());
  }

  public final boolean hasPowerInHand(final Player player) {
    return this.isSimilar(player.getItemInHand());
  }
}
