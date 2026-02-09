package dev.kurai.uhc;

import org.bukkit.plugin.java.JavaPlugin;

public final class UltraHardcorePlugin extends JavaPlugin {

  private final UltraHardcoreAPI ultraHardcore;

  public UltraHardcorePlugin() {
    this.ultraHardcore = UltraHardcoreAPI.create(new UltraHardcoreEngine(this));
  }

  @Override
  public void onLoad() {
    this.ultraHardcore.onLoad();
  }

  @Override
  public void onEnable() {
    this.ultraHardcore.onEnable();
  }

  @Override
  public void onDisable() {
    this.ultraHardcore.onDisable();
  }
}
