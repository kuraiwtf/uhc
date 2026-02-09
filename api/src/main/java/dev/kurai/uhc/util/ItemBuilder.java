package dev.kurai.uhc.util;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ItemBuilder {

  private static final LegacyComponentSerializer SERIALIZER =
      LegacyComponentSerializer.legacyAmpersand();

  private ItemStack itemStack;

  public ItemBuilder(@NotNull final ItemStack itemStack) {
    this.itemStack = itemStack.clone();
  }

  public ItemBuilder(final Material material) {
    this(material, 1);
  }

  public ItemBuilder(final Material material, final int amount) {
    this(material, amount, 0);
  }

  public ItemBuilder(final Material material, final int amount, final int data) {
    this(new ItemStack(material, amount, (short) data));
  }

  public static boolean hasTag(final ItemStack itemStack, final String key) {
    final var copy = CraftItemStack.asNMSCopy(itemStack);
    final var tag = (copy.getTag() == null ? new NBTTagCompound() : copy.getTag());
    return tag.hasKey(key);
  }

  public static String getTag(final ItemStack itemStack, final String key) {
    final var copy = CraftItemStack.asNMSCopy(itemStack);
    final var tag = (copy.getTag() == null ? new NBTTagCompound() : copy.getTag());
    return tag.getString(key);
  }

  @Override
  public ItemBuilder clone() {
    return new ItemBuilder(this.itemStack.clone());
  }

  public ItemBuilder lunarTag(final String key, final Object value) {
    final var copy = CraftItemStack.asNMSCopy(this.itemStack);
    final var tag = (copy.hasTag() ? copy.getTag() : new NBTTagCompound());

    final var lunarTag = (tag.hasKey("lunar") ? tag.getCompound("lunar") : new NBTTagCompound());

    if (value instanceof String) {
      lunarTag.setString(key, (String) value);
    } else if (value instanceof Integer) {
      lunarTag.setInt(key, (Integer) value);
    } else if (value instanceof Double) {
      lunarTag.setDouble(key, (Double) value);
    } else if (value instanceof Float) {
      lunarTag.setFloat(key, (Float) value);
    } else if (value instanceof Boolean) {
      lunarTag.setBoolean(key, (Boolean) value);
    } else if (value instanceof Long) {
      lunarTag.setLong(key, (Long) value);
    } else if (value instanceof Byte) {
      lunarTag.setByte(key, (Byte) value);
    } else if (value instanceof Short) {
      lunarTag.setShort(key, (Short) value);
    }

    tag.set("lunar", lunarTag);
    copy.setTag(tag);

    this.itemStack.setItemMeta(CraftItemStack.getItemMeta(copy));
    return this;
  }

  public ItemBuilder tag(final String key, final String value) {
    final var copy = CraftItemStack.asNMSCopy(this.itemStack);
    final var tag = (copy.getTag() == null ? new NBTTagCompound() : copy.getTag());
    tag.setString(key, value);
    copy.setTag(tag);
    this.itemStack.setItemMeta(CraftItemStack.getItemMeta(copy));
    return this;
  }

  public ItemBuilder addFlags(final @NotNull ItemFlag... flags) {
    final var meta = this.itemStack.getItemMeta();
    meta.addItemFlags(flags);
    this.itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder removeFlags(final @NotNull ItemFlag... flags) {
    final var meta = this.itemStack.getItemMeta();
    meta.removeItemFlags(flags);
    this.itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder enchant(final Enchantment enchantment, final int level) {
    if (this.itemStack.getType() == Material.ENCHANTED_BOOK) {
      final var meta = (EnchantmentStorageMeta) this.itemStack.getItemMeta();
      meta.addStoredEnchant(enchantment, level, true);
      this.itemStack.setItemMeta(meta);
    } else {
      this.itemStack.addUnsafeEnchantment(enchantment, level);
    }
    return this;
  }

  public ItemBuilder data(final int data) {
    this.itemStack.setDurability((short) data);
    return this;
  }

  public ItemBuilder amount(final int amount) {
    this.itemStack.setAmount(amount);
    return this;
  }

  public ItemBuilder name(final String displayName) {
    final var meta = this.itemStack.getItemMeta();
    meta.setDisplayName(CC.colorize("&f" + displayName));
    this.itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder name(final Component component) {
    return this.name(SERIALIZER.serialize(component));
  }

  public ItemBuilder lore(final String @NotNull ... lines) {
    return this.lore(Arrays.asList(lines));
  }

  @Contract("_ -> this")
  public ItemBuilder lore(@NotNull final Collection<String> lines) {
    final var lore = Lists.<String>newArrayList();
    for (final var line : lines) {
      for (final String str :
          line.replace("\n", "%newline%")
              .replace("<newline>", "%newline%")
              .replace("<nl>", "%newline%")
              .replace("<line>", "%newline%")
              .split("%newline%")) {
        lore.add(CC.colorize("&f" + str));
      }
    }

    final var meta = this.itemStack.getItemMeta();
    meta.setLore(lore);
    this.itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder lore(final String @NotNull [] line, final String... lines) {
    final var lore = Lists.<String>newArrayList();
    lore.addAll(Arrays.asList(line));
    lore.addAll(Arrays.asList(lines));
    return this.lore(lore);
  }

  public ItemBuilder lore(final Component... components) {
    return this.lore(Arrays.stream(components).map(SERIALIZER::serialize).toList());
  }

  public ItemBuilder lore(final List<Component> components) {
    return this.lore(components.stream().map(SERIALIZER::serialize).toList());
  }

  public ItemBuilder unbreakable(final boolean unbreakable) {
    final var meta = this.itemStack.getItemMeta();
    meta.spigot().setUnbreakable(unbreakable);
    this.itemStack.setItemMeta(meta);
    return this;
  }

  public ItemBuilder glowing(final boolean glowing) {
    if (glowing) {
      this.itemStack.addUnsafeEnchantment(Enchantment.LUCK, 1);
      final var itemMeta = this.itemStack.getItemMeta();
      itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
      this.itemStack.setItemMeta(itemMeta);
    } else {
      this.itemStack.removeEnchantment(Enchantment.LUCK);
    }
    return this;
  }

  public ItemBuilder skullOwner(final String owner) {
    final var skullMeta = (SkullMeta) this.itemStack.getItemMeta();
    skullMeta.setOwner(owner);
    this.itemStack.setItemMeta(skullMeta);
    return this;
  }

  public ItemBuilder url(final String url) {
    final var profile = new GameProfile(UUID.randomUUID(), null);
    final var propertyMap = profile.getProperties();
    propertyMap.put("textures", new Property("textures", url));
    final var skullMeta = (SkullMeta) this.itemStack.getItemMeta();
    final Class<?> skullMetaClass = skullMeta.getClass();

    try {
      final Field fakeProfile = skullMetaClass.getDeclaredField("profile");
      fakeProfile.setAccessible(true);
      fakeProfile.set(skullMeta, profile);
      fakeProfile.setAccessible(false);
      this.itemStack.setItemMeta(skullMeta);
      return this;
    } catch (final IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
    }
    return this;
  }

  public ItemBuilder attributeModifier(final String name, final double value) {
    final var nmsItem = CraftItemStack.asNMSCopy(this.itemStack);

    final var tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

    final var modifiers = new NBTTagList();
    final var modifier = new NBTTagCompound();

    modifier.setString("AttributeName", name);
    modifier.setString("Name", name);
    modifier.setDouble("Amount", value);
    modifier.setInt("Operation", 0);
    modifier.setLong("UUIDMost", 894654);
    modifier.setLong("UUIDLeast", 2872);
    modifier.setString("Slot", "mainhand");

    modifiers.add(modifier);
    tag.set("AttributeModifiers", modifiers);
    nmsItem.setTag(tag);

    this.itemStack = CraftItemStack.asBukkitCopy(nmsItem);
    return this;
  }

  public ItemStack asItemStack() {
    return this.itemStack;
  }
}
