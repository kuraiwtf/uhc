package dev.kurai.uhc.util;

import static dev.kurai.uhc.util.CC.HEART;
import static java.lang.String.*;
import static net.kyori.adventure.text.Component.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public final class PlayerUtil {

  public static double getHealth(final Player player) {
    return player.getHealth();
  }

  public static float getAbsorption(final Player player) {
    return ((CraftPlayer) player).getHandle().getAbsorptionHearts();
  }

  public static double getEntireHealth(final Player player) {
    return getHealth(player) + getAbsorption(player);
  }

  public static Component formatHealthAsPercentage(
      final Player player, final boolean complete, final Style style) {
    final double health = complete ? getEntireHealth(player) : getHealth(player);
    return text("%.2f".formatted(health / player.getMaxHealth() * 100))
        .style(style)
        .append(text('%').decoration(TextDecoration.BOLD, false));
  }

  public static Component formatHealthAsHearts(
      final Player player,
      final boolean complete,
      final Style currentHealthStyle,
      final Style maxHealthStyle,
      final Component separator) {
    final double health = complete ? getEntireHealth(player) : getHealth(player);
    return text()
        .append(text(health).style(currentHealthStyle))
        .append(text(HEART).style(currentHealthStyle).decoration(TextDecoration.BOLD, false))
        .append(separator)
        .append(text(player.getMaxHealth()).style(maxHealthStyle))
        .append(text(HEART).style(maxHealthStyle).decoration(TextDecoration.BOLD, false))
        .build();
  }

  public static Component formatHealthAsHeartBar(
      final Player player,
      final Style fullHeartStyle,
      final Style halfHeartStyle,
      final Style absorptionHeartStyle,
      final Style emptyHeartStyle) {
    final double health = getEntireHealth(player);
    final double absorption = getAbsorption(player);

    final int fullHearts = (int) Math.floor(health / 2.0);
    final boolean hasHalfHeart = (health % 2.0) >= 1.0;
    final int absorptionHearts = (int) Math.ceil(absorption / 2.0);
    final int maxHearts = 10;
    final int emptyHearts =
        Math.max(0, maxHearts - fullHearts - (hasHalfHeart ? 1 : 0) - absorptionHearts);

    final var result = text();

    for (int i = 0; i < fullHearts; i++) {
      result.append(text(HEART).style(fullHeartStyle));
    }

    if (hasHalfHeart) {
      result.append(text(HEART).style(halfHeartStyle));
    }

    for (int i = 0; i < emptyHearts; i++) {
      result.append(text(HEART).style(emptyHeartStyle));
    }

    for (int i = 0; i < absorptionHearts; i++) {
      result.append(text(HEART).style(absorptionHeartStyle));
    }

    return result.build();
  }

  public static void updateItem(final Player player, final int slot) {
    if (slot == -1) {
      return;
    }

    final var packet =
        new PacketPlayOutSetSlot(
            0, retrieveIndex(slot), CraftItemStack.asNMSCopy(player.getInventory().getItem(slot)));

    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }

  public static void updateHeldItem(final Player player) {
    updateItem(player, retrieveIndex(player.getInventory().getHeldItemSlot()));
  }

  private static int retrieveIndex(int index) {
    if (index < 9) {
      index = index + 36;
    } else if (index > 35) {
      index = 8 - (index - 36);
    }
    return index;
  }
}
