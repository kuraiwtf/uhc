package dev.kurai.uhc.game.rule;

import dev.kurai.uhc.util.api.Identifiable;
import dev.kurai.uhc.util.api.name.Nameable;
import org.bukkit.inventory.ItemStack;

public final class GameRule implements Identifiable<String>, Nameable<String> {

  private final String identifier;
  private final String name;

  private final ItemStack icon;

  private boolean state;

  public GameRule(final String identifier, final String name, final ItemStack icon) {
    this.identifier = identifier;
    this.name = name;

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
