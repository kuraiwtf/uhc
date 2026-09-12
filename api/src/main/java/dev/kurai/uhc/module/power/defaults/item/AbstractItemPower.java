package dev.kurai.uhc.module.power.defaults.item;

import com.google.common.collect.Lists;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.restriction.defaults.CooldownPowerRestriction;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

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
    final var lore = Lists.<String>newArrayList();
    lore.add("");
    lore.addAll(this.lore());
    lore.add("");

    return new ItemBuilder(this.provideIcon(player))
        .name("&8&l»%s &l%s&8 &l«".formatted(this.getColor().asBukkitColor(), this.name))
        .lore(lore)
        .tag(NBT_TAG, this.identifier)
        .lunarTag("glint", this.provideGlint(player))
        .asItemStack();
  }

  private Object provideGlint(final Player player) {
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
        && ItemBuilder.hasTag(itemStack, NBT_TAG)
        && ItemBuilder.getTag(itemStack, NBT_TAG).equals(this.getId());
  }

  public boolean hasPowerInHand(final @NonNull Player player) {
    final ItemStack hand = player.getItemInHand();
    if (hand == null || hand.getType() == Material.AIR) {
      return false;
    }

    return ItemBuilder.hasTag(hand, NBT_TAG)
        && ItemBuilder.getTag(hand, NBT_TAG).equals(this.getId());
  }
}
