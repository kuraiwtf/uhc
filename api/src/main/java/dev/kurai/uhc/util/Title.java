package dev.kurai.uhc.util;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Constructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

public final class Title {

  private Title() {}

  private static void sendPacket(final Player player, final Object packet) {
    try {
      Object handle = player.getClass().getMethod("getHandle").invoke(player);
      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
      playerConnection
          .getClass()
          .getMethod("sendPacket", getNMSClass("Packet"))
          .invoke(playerConnection, packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Class<?> getNMSClass(final String name) {
    final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    try {
      return Class.forName("net.minecraft.server." + version + "." + name);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void sendTitle(
      final Player player,
      final Integer fadeIn,
      final Integer stay,
      final Integer fadeOut,
      @Nullable String title,
      @Nullable String subtitle) {
    try {
      if (title != null) {
        Object e =
            getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
        Object chatTitle =
            getNMSClass("IChatBaseComponent")
                .getDeclaredClasses()[0]
                .getMethod("a", String.class)
                .invoke(null, "{\"text\":\"" + title + "\"}");
        Constructor<?> subtitleConstructor =
            getNMSClass("PacketPlayOutTitle")
                .getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClass("IChatBaseComponent"),
                    Integer.TYPE,
                    Integer.TYPE,
                    Integer.TYPE);
        Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
        sendPacket(player, titlePacket);
        e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
        chatTitle =
            getNMSClass("IChatBaseComponent")
                .getDeclaredClasses()[0]
                .getMethod("a", String.class)
                .invoke(null, "{\"text\":\"" + title + "\"}");
        subtitleConstructor =
            getNMSClass("PacketPlayOutTitle")
                .getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClass("IChatBaseComponent"));
        titlePacket = subtitleConstructor.newInstance(e, chatTitle);
        sendPacket(player, titlePacket);
      }

      if (subtitle != null) {
        Object e =
            getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
        Object chatSubtitle =
            getNMSClass("IChatBaseComponent")
                .getDeclaredClasses()[0]
                .getMethod("a", String.class)
                .invoke(null, "{\"text\":\"" + title + "\"}");
        Constructor<?> subtitleConstructor =
            getNMSClass("PacketPlayOutTitle")
                .getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClass("IChatBaseComponent"),
                    Integer.TYPE,
                    Integer.TYPE,
                    Integer.TYPE);
        Object subtitlePacket =
            subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
        sendPacket(player, subtitlePacket);
        e =
            getNMSClass("PacketPlayOutTitle")
                .getDeclaredClasses()[0]
                .getField("SUBTITLE")
                .get(null);
        chatSubtitle =
            getNMSClass("IChatBaseComponent")
                .getDeclaredClasses()[0]
                .getMethod("a", String.class)
                .invoke(null, "{\"text\":\"" + subtitle + "\"}");
        subtitleConstructor =
            getNMSClass("PacketPlayOutTitle")
                .getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClass("IChatBaseComponent"),
                    Integer.TYPE,
                    Integer.TYPE,
                    Integer.TYPE);
        subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
        sendPacket(player, subtitlePacket);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void clearTitle(final Player player) {
    sendTitle(requireNonNull(player, "Player must not be null"), 0, 0, 0, "", "");
  }
}
