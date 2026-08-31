package dev.kurai.uhc.skin;

import com.google.common.collect.Maps;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.util.Skin;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

final class SkinHolderImpl implements SkinHolder {

  private final Profile profile;
  private final Map<UUID, Skin> skins;

  SkinHolderImpl(final Profile profile) {
    this.profile = profile;
    this.skins = Maps.newHashMap();
  }

  @Override
  public @Nullable Skin skin(final UUID uniqueId) {
    return this.skins.get(uniqueId);
  }

  @Override
  public void applySkin(final UUID uniqueId, final Skin skin) {
    this.skins.put(uniqueId, skin);
    this.updateSkin();
  }

  @Override
  public void removeSkin(final UUID uniqueId) {
    this.skins.remove(uniqueId);
  }

  @Override
  public void updateSkin() {
    this.profile.executeAction(
        player -> {
          for (final Player receiver : Bukkit.getOnlinePlayers()) {
            receiver.hidePlayer(player);
            receiver.showPlayer(player);
          }
        });
  }
}
