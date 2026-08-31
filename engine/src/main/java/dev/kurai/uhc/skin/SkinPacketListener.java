package dev.kurai.uhc.skin;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.SkinComponent;
import dev.kurai.uhc.util.Skin;
import org.bukkit.entity.Player;

final class SkinPacketListener implements PacketListener {

  private final ProfileService profileService;
  private final SkinService skinService;

  SkinPacketListener(final ProfileService profileService, final SkinService skinService) {
    this.profileService = profileService;
    this.skinService = skinService;
  }

  @Override
  public void onPacketSend(final PacketSendEvent event) {
    if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
      final WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(event);
      final var playerInfoDataList = packet.getPlayerDataList();
      final Player receiver = event.getPlayer();
      final SkinHolder skinHolder = this.skinService.holder(receiver.getUniqueId());
      playerInfoDataList.stream()
          .filter(playerData -> playerData.getUserProfile() != null)
          .forEach(
              data -> {
                final UserProfile userProfile = data.getUserProfile();
                Skin customSkin = skinHolder.skin(userProfile.getUUID());
                if (customSkin == null) {
                  final Profile profile =
                      this.profileService.getOrCreateProfile(userProfile.getUUID());
                  final SkinComponent component = profile.getComponent(SkinComponent.class);
                  if (component == null) {
                    return;
                  }

                  customSkin = component.skin();
                }

                final String newValue = customSkin.getSkinValue();
                final String newSignature = customSkin.getSkinSignature();
                final TextureProperty newTextureProperty =
                    new TextureProperty("textures", newValue, newSignature);
                userProfile
                    .getTextureProperties()
                    .removeIf(textureProperty -> textureProperty.getName().equals("textures"));
                userProfile.getTextureProperties().add(newTextureProperty);
              });
      event.markForReEncode(true);
    }
  }
}
