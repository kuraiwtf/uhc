package dev.kurai.uhc.tablist.part;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

import dev.kurai.uhc.module.service.ModuleService;
import dev.kurai.uhc.tablist.TabListPart;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

@RequiredArgsConstructor
public final class CreditTabListPart implements TabListPart {

  private final ModuleService moduleService;

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
        .append(text(this.moduleService.getCurrentModule().developer(), AQUA))
        .build();
  }

  @Contract(pure = true)
  @Override
  public Position position() {
    return Position.BOTTOM;
  }
}
