package dev.kurai.uhc.tablist;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface TabListPart {

  /**
   * Retrieves the unique key identifier for this game tab list part. The key is used to associate
   * and manage specific parts in the game tab list.
   *
   * @return the unique key as a string.
   */
  String key();

  /**
   * Retrieves the priority level for this game tab list part. A lower value indicates a higher
   * priority, with 0 as the default.
   *
   * @return the priority level as an integer.
   */
  default int priority() {
    return 0;
  }

  /**
   * Provides the lines of text to be displayed for a specific player in the tab list.
   *
   * @param player the player for whom the tab list lines are being provided
   * @return a {@code Component} representing the lines to display for the specified player
   */
  Component render(final Player player);

  /**
   * Retrieves the position of this game tab list part. The position determines whether the content
   * will be displayed at the top or bottom of the game tab list.
   *
   * @return the position of the game tab list part, either {@code TOP} or {@code BOTTOM}.
   */
  Position position();

  enum Position {
    TOP,
    BOTTOM
  }
}
