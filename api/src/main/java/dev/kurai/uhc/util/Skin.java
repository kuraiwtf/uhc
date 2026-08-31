package dev.kurai.uhc.util;

import com.mojang.authlib.GameProfile;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class Skin {

  private String value = "";
  private String signature = "";

  public Skin(final Player player) {
    final GameProfile gameProfile = ((CraftPlayer) player).getHandle().getProfile();
    gameProfile.getProperties().get("textures").stream()
        .findAny()
        .ifPresent(
            property -> {
              this.value = property.getValue();
              this.signature = property.getSignature();
            });
  }

  public String getSkinValue() {
    return this.value;
  }

  public String getSkinSignature() {
    return this.signature;
  }
}
