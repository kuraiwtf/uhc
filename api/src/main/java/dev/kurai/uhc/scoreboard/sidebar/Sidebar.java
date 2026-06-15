package dev.kurai.uhc.scoreboard.sidebar;

import net.kyori.adventure.text.Component;

public interface Sidebar {

  void editTitle(final Component title);

  void overrideLine(final int score, final Component content);

  void removeLine(final int score);

  void destroy();

  void send();
}
