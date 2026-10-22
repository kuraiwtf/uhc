package dev.kurai.uhc.team;

import dev.kurai.uhc.util.Color;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record TeamStyle(Color color, @Nullable String symbol) {

  private static final List<Color> COLORS =
      List.of(
          Color.RED,
          Color.BLUE,
          Color.GREEN,
          Color.YELLOW,
          Color.AQUA,
          Color.LIGHT_PURPLE,
          Color.GOLD,
          Color.DARK_GREEN,
          Color.DARK_AQUA,
          Color.DARK_PURPLE,
          Color.DARK_RED,
          Color.DARK_BLUE,
          Color.GRAY,
          Color.WHITE);

  private static final List<String> SYMBOLS = List.of("♠", "♥", "♦", "♣", "★", "☀", "☾", "✿");

  public static final List<TeamStyle> POOL = buildPool();

  private static List<TeamStyle> buildPool() {
    final var pool = new ArrayList<TeamStyle>();
    for (final var color : COLORS) {
      pool.add(new TeamStyle(color, null));
    }
    for (final var symbol : SYMBOLS) {
      for (final var color : COLORS) {
        pool.add(new TeamStyle(color, symbol));
      }
    }
    return List.copyOf(pool);
  }

  public String displayName() {
    return this.symbol == null ? this.color.name() : this.symbol + " " + this.color.name();
  }

  public String coloredName() {
    return this.color.asBukkitColor() + this.displayName();
  }
}
