package dev.kurai.uhc.profile.component;

import dev.kurai.uhc.ecs.component.Component;

public final class ProfileMiningComponent implements Component {

  private int stoneMined;
  private int ironMined;
  private int goldMined;
  private int diamondMined;

  public ProfileMiningComponent() {}

  public ProfileMiningComponent(
      final int stoneMined, final int ironMined, final int goldMined, final int diamondMined) {
    this.stoneMined = stoneMined;
    this.ironMined = ironMined;
    this.goldMined = goldMined;
    this.diamondMined = diamondMined;
  }

  public int getStoneMined() {
    return this.stoneMined;
  }

  public void setStoneMined(final int stoneMined) {
    this.stoneMined = stoneMined;
  }

  public int getIronMined() {
    return this.ironMined;
  }

  public void setIronMined(final int ironMined) {
    this.ironMined = ironMined;
  }

  public int getGoldMined() {
    return this.goldMined;
  }

  public void setGoldMined(final int goldMined) {
    this.goldMined = goldMined;
  }

  public int getDiamondMined() {
    return this.diamondMined;
  }

  public void setDiamondMined(final int diamondMined) {
    this.diamondMined = diamondMined;
  }
}
