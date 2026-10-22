package dev.kurai.uhc.module.camp;

import dev.kurai.uhc.util.Color;
import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.annotation.Name;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.jspecify.annotations.Nullable;

public abstract class AbstractCampData<T extends AbstractCamp<?>>
    implements Identifiable<Class<? extends T>> {

  protected final Class<? extends T> identifier;

  protected AbstractCampData(final Class<? extends T> team) {
    this.identifier = team;
  }

  @Override
  public Class<? extends T> getId() {
    return this.identifier;
  }

  /** Display name of the camp, read from {@link Name} by default. */
  public String getName() {
    final var name = this.identifier.getAnnotation(Name.class);
    return name == null ? this.identifier.getSimpleName() : name.value();
  }

  public TextColor getTextColor() {
    return NamedTextColor.WHITE;
  }

  public ChatColor getChatColor() {
    return ChatColor.WHITE;
  }

  public @Nullable Color getColor() {
    return Color.WHITE;
  }
}
