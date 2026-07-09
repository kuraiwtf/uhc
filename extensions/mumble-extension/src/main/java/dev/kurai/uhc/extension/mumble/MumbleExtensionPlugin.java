package dev.kurai.uhc.extension.mumble;

import dev.kurai.uhc.UltraHardcoreAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class MumbleExtensionPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    new MumbleExtension(UltraHardcoreAPI.getInstance()).init();
  }
}
