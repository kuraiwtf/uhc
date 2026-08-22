package dev.kurai.uhc.game.configuration.border;

public enum BorderType {
  DAMAGE("Dégâts"),
  TELEPORT("Téléportation"),
  ;

  private final String name;

  BorderType(final String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
