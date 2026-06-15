package dev.kurai.uhc.scoreboard.sidebar;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Sidebar {

  void editTitle(final  Component title);

  void overrideLine(final int score, final  Component content);

  void removeLine(final int score);

  void destroy();

  void send();
}
