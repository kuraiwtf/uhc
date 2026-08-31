package dev.kurai.uhc.nickname;

import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;

public enum NicknameStyle {
  BOLD(ChatColor.BOLD, TextDecoration.BOLD),
  ITALIC(ChatColor.ITALIC, TextDecoration.ITALIC),
  UNDERLINED(ChatColor.UNDERLINE, TextDecoration.UNDERLINED),
  OBFUSCATED(ChatColor.MAGIC, TextDecoration.OBFUSCATED),
  STRIKETHROUGH(ChatColor.STRIKETHROUGH, TextDecoration.STRIKETHROUGH),
  ;

  private final char character;
  private final TextDecoration decoration;

  NicknameStyle(final ChatColor chatColor, final TextDecoration decoration) {
    this.character = chatColor.getChar();
    this.decoration = decoration;
  }

  public String character() {
    return "§" + this.character;
  }

  public TextDecoration decoration() {
    return this.decoration;
  }
}
