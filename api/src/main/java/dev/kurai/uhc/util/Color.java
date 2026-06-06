package dev.kurai.uhc.util;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public final class Color {

  public static final Color WHITE = new Color(ChatColor.WHITE, 255, 255, 255);
  public static final Color BLACK = new Color(ChatColor.BLACK, 0, 0, 0);
  public static final Color DARK_BLUE = new Color(ChatColor.DARK_BLUE, 0, 0, 170);
  public static final Color DARK_GREEN = new Color(ChatColor.DARK_GREEN, 0, 170, 0);
  public static final Color DARK_AQUA = new Color(ChatColor.DARK_AQUA, 0, 170, 170);
  public static final Color DARK_RED = new Color(ChatColor.DARK_RED, 170, 0, 0);
  public static final Color DARK_PURPLE = new Color(ChatColor.DARK_PURPLE, 170, 0, 170);
  public static final Color GOLD = new Color(ChatColor.GOLD, 255, 170, 0);
  public static final Color GRAY = new Color(ChatColor.GRAY, 170, 170, 170);
  public static final Color DARK_GRAY = new Color(ChatColor.DARK_GRAY, 85, 85, 85);
  public static final Color BLUE = new Color(ChatColor.BLUE, 85, 85, 255);
  public static final Color GREEN = new Color(ChatColor.GREEN, 85, 255, 85);
  public static final Color AQUA = new Color(ChatColor.AQUA, 85, 255, 255);
  public static final Color RED = new Color(ChatColor.RED, 255, 85, 85);
  public static final Color LIGHT_PURPLE = new Color(ChatColor.LIGHT_PURPLE, 255, 85, 255);
  public static final Color YELLOW = new Color(ChatColor.YELLOW, 255, 255, 85);

  private final ChatColor color;
  private final int red, green, blue;

  public ChatColor asBukkitColor() {
    return this.color;
  }

  public TextColor asAdventureColor() {
    return TextColor.color(this.red, this.green, this.blue);
  }

  public java.awt.Color asJavaColor() {
    return new java.awt.Color(this.red, this.green, this.blue);
  }
}
