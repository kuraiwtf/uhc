package dev.kurai.uhc.tablist.part;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.tablist.TabListPart;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

public final class CreditTabListPart implements TabListPart {

  @Contract(pure = true)
  @Override
  public String key() {
    return "credit";
  }

  @Override
  public int priority() {
    return 1000;
  }

  @Contract("_ -> new")
  @Override
  public @Unmodifiable Component render(final Player player) {
    return text()
        .append(text("Développé par "))
        .append(text('@', DARK_AQUA))
        .append(text("kuraiwtf", AQUA))
        .build();
  }

  @Contract(pure = true)
  @Override
  public Position position() {
    return Position.BOTTOM;
  }
}
