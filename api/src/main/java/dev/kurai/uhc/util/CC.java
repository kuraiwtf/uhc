package dev.kurai.uhc.util;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;

public final class CC {

  public static final String BAR = "❘";
  public static final String BAR_2 = "▎";
  public static final String BAR_3 = "▍";
  public static final String SQUARE = "▪";
  public static final String SQUARE_EMPTY = "▫";
  public static final String BIG_SQUARE = "■";
  public static final String DANGER = "⚠";
  public static final String HEART = "❤";
  public static final String INFO = "ⓘ";
  public static final String ARROW_RIGHT = "➽";
  public static final String BURGER = "☰";
  public static final String PEN = "✎";
  public static final String BERRY = "❦";
  public static final String COINS = "⛃";
  public static final String CHECKMARK = "✔";
  public static final String CROSS = "✖";
  public static final String INFINITE = "∞";
  public static final String CURVED_ARROW_RIGHT = "➥";
  public static final String BIG_ARROW_RIGHT = "➲";

  public static final String SUN = "✴";
  public static final String BIG_SUN = "✹";

  public static final String STAR = "✰";
  public static final String BIG_STAR = "✪";

  public static final String FLOWER = "✿";

  public static final String DIAMOND = "✦";
  public static final String DIAMOND_2 = "◊";

  public static final String CIRCLE = "•";

  public static final String YIN_YANG = "☯";

  public static final String DIRECTIONAL_CROSS = "❖";

  public static final String ONE_CIRCLE_EMPTY = "①";
  public static final String TWO_CIRCLE_EMPTY = "②";
  public static final String THREE_CIRCLE_EMPTY = "③";
  public static final String FOUR_CIRCLE_EMPTY = "④";
  public static final String FIVE_CIRCLE_EMPTY = "⑤";
  public static final String SIX_CIRCLE_EMPTY = "⑥";
  public static final String SEVEN_CIRCLE_EMPTY = "⑦";
  public static final String EIGHT_CIRCLE_EMPTY = "⑧";
  public static final String NINE_CIRCLE_EMPTY = "⑨";
  public static final String TEN_CIRCLE_EMPTY = "⑩";

  public static final String ONE_CIRCLE_FILL = "❶";
  public static final String TWO_CIRCLE_FILL = "❷";
  public static final String THREE_CIRCLE_FILL = "❸";
  public static final String FOUR_CIRCLE_FILL = "❹";
  public static final String FIVE_CIRCLE_FILL = "❺";
  public static final String SIX_CIRCLE_FILL = "❻";
  public static final String SEVEN_CIRCLE_FILL = "❼";
  public static final String EIGHT_CIRCLE_FILL = "❽";
  public static final String NINE_CIRCLE_FILL = "❾";
  public static final String TEN_CIRCLE_FILL = "❿";

  /** The default center padding */
  public static final int CENTER_PX = 152;

  /** The vertical lines a player can see at once in his chat history */
  public static final int VISIBLE_CHAT_LINES = 20;

  public static final String BUILTIN_PREFIX = "&e&lUHC&7 &l" + BAR + "&f";

  private CC() {}

  public static TextComponent.Builder prefix() {
    return text().append(text(BAR, GRAY, BOLD)).appendSpace();
  }

  public static String prefix(final String text) {
    return prefix(text, BUILTIN_PREFIX);
  }

  public static String prefix(final String text, final String prefix) {
    return colorize(prefix + " " + text);
  }

  /**
   * Centers a message automatically for padding {@link #CENTER_PX}
   *
   * @param message
   * @return
   */
  public static String center(final String message) {
    return center(message, ' ');
  }

  /**
   * Centers a message for padding using the given center px
   *
   * @param message
   * @param centerPx
   * @return
   */
  public static String center(final String message, final int centerPx) {
    return center(message, ' ', centerPx);
  }

  /**
   * Centers a message for padding {@link #CENTER_PX} with the given space character colored by the
   * given chat color, example:
   *
   * <p>================= My Centered Message ================= (if the space is '=')
   *
   * @param message
   * @param space
   * @return
   */
  public static String center(final String message, final char space) {
    return center(message, space, CENTER_PX);
  }

  /**
   * Centers a message according to the given space character, color and padding
   *
   * @param message
   * @param space
   * @param centerPx
   * @return
   */
  public static String center(final String message, final char space, final int centerPx) {
    if (message == null || message.isEmpty()) {
      return "";
    }

    var messagePxSize = 0;

    var previousCode = false;
    var isBold = false;

    for (final var c : message.toCharArray()) {
      if (c == '&' || c == ChatColor.COLOR_CHAR) {
        previousCode = true;
      } else if (previousCode) {
        previousCode = false;
        if (c == 'l' || c == 'L') {
          isBold = true;
          continue;
        }
        isBold = false;
      } else {
        final var defaultFont = DefaultFontInfo.getDefaultFontInfo(c);
        messagePxSize += isBold ? defaultFont.getBoldLength() : defaultFont.getLength();
        messagePxSize++;
      }
    }

    final var builder = new StringBuilder();
    final var halvedMessageSize = messagePxSize / 2;
    final var toCompensate = centerPx - halvedMessageSize;
    final var font = DefaultFontInfo.getDefaultFontInfo(space);
    final var spaceLength = isBold ? font.getBoldLength() : font.getLength();

    var compensated = 0D;
    while (compensated < toCompensate) {
      builder.append(space);

      compensated += spaceLength;
    }

    return colorize(builder + " " + message + " " + builder);
  }

  /**
   * Centers a message automatically for padding {@link #CENTER_PX}
   *
   * @param message the component to center
   * @return centered component
   */
  public static Component center(final Component message) {
    return center(message, ' ');
  }

  /**
   * Centers a message for padding using the given center px
   *
   * @param message the component to center
   * @param centerPx the center padding in pixels
   * @return centered component
   */
  public static Component center(final Component message, final int centerPx) {
    return center(message, ' ', centerPx);
  }

  /**
   * Centers a message for padding {@link #CENTER_PX} with the given space character, example:
   *
   * <p>================= My Centered Message ================= (if the space is '=')
   *
   * @param message the component to center
   * @param space the spacing character
   * @return centered component
   */
  public static Component center(final Component message, final char space) {
    return center(message, space, CENTER_PX);
  }

  /**
   * Centers a message according to the given space character and padding
   *
   * @param message the component to center
   * @param space the spacing character
   * @param centerPx the center padding in pixels
   * @return centered component
   */
  public static Component center(final Component message, final char space, final int centerPx) {
    if (message == null || message.equals(Component.empty())) {
      return Component.empty();
    }

    var messagePxSize = 0;
    messagePxSize = calculateComponentPixelSize(message);

    final var halvedMessageSize = messagePxSize / 2;
    final var toCompensate = centerPx - halvedMessageSize;
    final var font = DefaultFontInfo.getDefaultFontInfo(space);
    final var spaceLength = font.getLength();

    var compensated = 0D;
    final var paddingBuilder = new StringBuilder();

    while (compensated < toCompensate) {
      paddingBuilder.append(space);
      compensated += spaceLength;
    }

    final var padding = paddingBuilder.toString();

    return Component.text()
        .append(Component.text(padding))
        .append(Component.space())
        .append(message)
        .append(Component.space())
        .append(Component.text(padding))
        .build();
  }

  /**
   * Calculates the pixel size of a component recursively
   *
   * @param component the component to measure
   * @return the pixel size
   */
  private static int calculateComponentPixelSize(final Component component) {
    var totalSize = 0;
    final var isBold = component.hasDecoration(TextDecoration.BOLD);

    if (component instanceof final TextComponent textComponent) {
      final var content = textComponent.content();

      for (final var c : content.toCharArray()) {
        final var defaultFont = DefaultFontInfo.getDefaultFontInfo(c);
        totalSize += isBold ? defaultFont.getBoldLength() : defaultFont.getLength();
        totalSize++; // Add spacing
      }
    }

    for (final var child : component.children()) {
      totalSize += calculateComponentPixelSize(child);
    }

    return totalSize;
  }

  @Contract("_ -> new")
  public static String colorize(final String entry) {
    return ChatColor.translateAlternateColorCodes('&', entry);
  }

  public static String stripColor(final String entry) {
    return ChatColor.stripColor(colorize(entry));
  }

  public static Component line(final TextColor first, final TextColor second) {
    final var segment = 21;

    final var component = text().decorate(TextDecoration.STRIKETHROUGH);
    for (var i = 0; i < segment; i++) {
      final TextColor color;
      if (i < segment / 3) {
        color = first;
      } else if (i < (2 * segment) / 3) {
        color = second;
      } else {
        color = WHITE;
      }
      component.append(text("-", color));
    }

    for (var j = 0; j < segment; j++) {
      final TextColor color;
      if (j < segment / 3) {
        color = WHITE;
      } else if (j < (2 * segment) / 3) {
        color = second;
      } else {
        color = first;
      }
      component.append(text("-", color));
    }

    return component.build();
  }

  public static String emptyLine(final ChatColor first, final ChatColor second) {
    final var segment = 21;

    final var builder = new StringBuilder();

    for (var i = 0; i < segment; i++) {
      final ChatColor color;
      if (i < segment / 3) {
        color = first;
      } else if (i < (2 * segment) / 3) {
        color = second;
      } else {
        color = ChatColor.WHITE;
      }
      builder.append(color).append("&m").append("-");
    }

    for (var j = 0; j < segment; j++) {
      final ChatColor color;
      if (j < segment / 3) {
        color = ChatColor.WHITE;
      } else if (j < (2 * segment) / 3) {
        color = second;
      } else {
        color = first;
      }
      builder.append(color).append("&m").append("-");
    }

    return CC.colorize(builder.toString());
  }

  public static String lineWithText(
      final String text, final ChatColor first, final ChatColor second) {
    final var start = new StringBuilder("--");
    final var end = new StringBuilder("--");

    final var textLength = ChatColor.stripColor(text).length();

    for (var i = 0; i < 18 - (textLength / 2); i++) {
      start.append("-");
      end.insert(0, "-");
    }

    if (textLength % 2 == 0) {
      end.insert(0, "-");
    }

    final var startBuilder = new StringBuilder("&f&m");
    for (var i = 0; i < start.length(); i++) {
      startBuilder.append(start.charAt(i));
      if (i == start.length() / 3) {
        startBuilder.append(first).append("&m");
      } else if (i == start.length() / 3 * 2) {
        startBuilder.append(second).append("&m");
      }
    }

    final var endBuilder = new StringBuilder(second + "&m");
    for (var i = 0; i < end.length(); i++) {
      endBuilder.append(end.charAt(i));
      if (i == end.length() / 3) {
        endBuilder.append(first).append("&m");
      } else if (i == end.length() / 3 * 2) {
        endBuilder.append("&f&m");
      }
    }

    return colorize(startBuilder + "&8 " + second + text + " &8" + endBuilder);
  }
}
