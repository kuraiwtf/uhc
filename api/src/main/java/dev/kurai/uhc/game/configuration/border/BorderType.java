package dev.kurai.uhc.game.configuration.border;

import org.jetbrains.annotations.NotNull;

public enum BorderType {
  DAMAGE("Dégâts"),
  TELEPORT("Téléportation"),
  ;

  private final String name;

  BorderType(final @NotNull String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
