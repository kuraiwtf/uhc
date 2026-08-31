package dev.kurai.uhc.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public final class Color {

  public static final Color WHITE =
      new Color("Blanc", ChatColor.WHITE, DyeColor.WHITE, NamedTextColor.WHITE, 255, 255, 255);
  public static final Color BLACK =
      new Color("Noir", ChatColor.BLACK, DyeColor.BLACK, NamedTextColor.BLACK, 0, 0, 0);
  public static final Color DARK_BLUE =
      new Color("Bleu Foncé", ChatColor.DARK_BLUE, DyeColor.BLUE, NamedTextColor.BLUE, 0, 0, 170);
  public static final Color DARK_GREEN =
      new Color(
          "Vert Foncé", ChatColor.DARK_GREEN, DyeColor.GREEN, NamedTextColor.DARK_GRAY, 0, 170, 0);
  public static final Color DARK_AQUA =
      new Color("Cyan", ChatColor.DARK_AQUA, DyeColor.CYAN, NamedTextColor.DARK_AQUA, 0, 170, 170);
  public static final Color DARK_RED =
      new Color(
          "Rouge Foncé", ChatColor.DARK_RED, DyeColor.RED, NamedTextColor.DARK_RED, 170, 0, 0);
  public static final Color DARK_PURPLE =
      new Color(
          "Violet",
          ChatColor.DARK_PURPLE,
          DyeColor.PURPLE,
          NamedTextColor.DARK_PURPLE,
          170,
          0,
          170);
  public static final Color GOLD =
      new Color("Orange", ChatColor.GOLD, DyeColor.ORANGE, NamedTextColor.GOLD, 255, 170, 0);
  public static final Color GRAY =
      new Color("Gris", ChatColor.GRAY, DyeColor.SILVER, NamedTextColor.GRAY, 170, 170, 170);
  public static final Color DARK_GRAY =
      new Color(
          "Gris Foncé", ChatColor.DARK_GRAY, DyeColor.GRAY, NamedTextColor.DARK_GRAY, 85, 85, 85);
  public static final Color BLUE =
      new Color("Bleu", ChatColor.BLUE, DyeColor.BLUE, NamedTextColor.BLUE, 85, 85, 255);
  public static final Color GREEN =
      new Color("Vert", ChatColor.GREEN, DyeColor.LIME, NamedTextColor.GREEN, 85, 255, 85);
  public static final Color AQUA =
      new Color(
          "Bleu Clair", ChatColor.AQUA, DyeColor.LIGHT_BLUE, NamedTextColor.AQUA, 85, 255, 255);
  public static final Color RED =
      new Color("Rouge", ChatColor.RED, DyeColor.RED, NamedTextColor.RED, 255, 85, 85);
  public static final Color LIGHT_PURPLE =
      new Color(
          "Rose",
          ChatColor.LIGHT_PURPLE,
          DyeColor.MAGENTA,
          NamedTextColor.LIGHT_PURPLE,
          255,
          85,
          255);
  public static final Color YELLOW =
      new Color("Jaune", ChatColor.YELLOW, DyeColor.YELLOW, NamedTextColor.YELLOW, 255, 255, 85);

  private final String name;
  private final ChatColor chatColor;
  private final DyeColor dyeColor;
  private final NamedTextColor namedColor;

  private final int red;
  private final int green;
  private final int blue;

  public Color(
      final String name,
      final ChatColor chatColor,
      final DyeColor dyeColor,
      final NamedTextColor namedColor,
      final int red,
      final int green,
      final int blue) {
    this.name = name;
    this.chatColor = chatColor;
    this.dyeColor = dyeColor;
    this.namedColor = namedColor;

    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public String name() {
    return this.name;
  }

  public ChatColor asBukkitColor() {
    return this.chatColor;
  }

  public DyeColor asDyeColor() {
    return this.dyeColor;
  }

  public TextColor asTextColor() {
    return TextColor.color(this.red, this.green, this.blue);
  }

  public NamedTextColor asNamedColor() {
    return this.namedColor;
  }

  public com.github.retrooper.packetevents.protocol.color.Color asPacketEventsColor() {
    return new com.github.retrooper.packetevents.protocol.color.Color(
        this.red, this.green, this.blue);
  }

  public java.awt.Color asJavaColor() {
    return new java.awt.Color(this.red, this.green, this.blue);
  }
}
