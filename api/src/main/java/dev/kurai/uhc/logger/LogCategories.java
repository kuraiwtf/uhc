package dev.kurai.uhc.logger;

import static dev.kurai.uhc.logger.LogCategory.logCategory;

import dev.kurai.uhc.util.Color;

public final class LogCategories {

  public static final LogCategory FIGHT = logCategory("Combat", Color.GOLD);
  public static final LogCategory KILL = logCategory("Kill", Color.GREEN);
  public static final LogCategory POWER = logCategory("Pouvoir", Color.LIGHT_PURPLE);
  public static final LogCategory ORE_MINING = logCategory("Minerai", Color.AQUA);
  public static final LogCategory COMMAND = logCategory("Commande", Color.GRAY);
}
