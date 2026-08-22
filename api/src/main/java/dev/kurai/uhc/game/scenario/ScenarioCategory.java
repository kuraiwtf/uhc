package dev.kurai.uhc.game.scenario;

import dev.kurai.uhc.util.Color;

public record ScenarioCategory(String name, Color color) {

  public static final ScenarioCategory COMBAT = new ScenarioCategory("Combat", Color.RED);
  public static final ScenarioCategory GAMEPLAY = new ScenarioCategory("Gameplay", Color.GREEN);
  public static final ScenarioCategory MINING = new ScenarioCategory("Minage", Color.GOLD);
  public static final ScenarioCategory UTILITY = new ScenarioCategory("Utilitaire", Color.AQUA);
}
