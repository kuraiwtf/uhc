package dev.kurai.uhc.game.rule;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public final class GameRule implements Identifiable<String>, Nameable<String> {

  private final String identifier;
  private final String name;

  private final ChatColor color;

  private final ItemStack icon;

  private boolean state;

  public GameRule(
      final String identifier, final String name, final ChatColor color, final ItemStack icon) {
    this.identifier = identifier;
    this.name = name;

    this.color = color;
    this.icon = icon;

    this.state = true;
  }

  @Override
  public String getId() {
    return this.identifier;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public ChatColor color() {
    return this.color;
  }

  public ItemStack icon() {
    return this.icon;
  }

  public boolean state() {
    return this.state;
  }

  public void state(final boolean state) {
    this.state = state;
  }
}
